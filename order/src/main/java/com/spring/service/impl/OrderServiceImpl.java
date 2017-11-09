package com.spring.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.reger.dubbo.annotation.Inject;
import com.spring.common.model.StatusCode;
import com.spring.common.model.exception.GlobalException;
import com.spring.common.model.model.ErrorInfo;
import com.spring.common.model.response.ObjectDataResponse;
import com.spring.domain.model.*;
import com.spring.domain.request.CancelRequest;
import com.spring.domain.request.PaymentRequest;
import com.spring.domain.request.PlaceOrderRequest;
import com.spring.domain.request.TccRequest;
import com.spring.domain.type.OrderStatus;
import com.spring.exception.PartialConfirmException;
import com.spring.exception.ReservationExpireException;
import com.spring.persistence.OrderMapper;
import com.spring.persistence.OrderParticipantMapper;
import com.spring.repository.ErrorRepository;
import com.spring.service.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @Description 订单service实现
 * @author ErnestCheng
 * @Date 2017/5/31.
 */
@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger= Logger.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderParticipantMapper orderParticipantMapper;
    @Inject
    private UserService userService;

    @Inject
    private UserBalanceTccService userBalanceTccService;

    @Inject
    private ProductService productService;

    @Inject
    private ProductStockTccService productStockTccService;

    @Inject
    private TccService tccService;


    @Autowired
    private ErrorRepository errorRepository;

    /**
     * 下预订单
     * @param request
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = RuntimeException.class)
    public ObjectDataResponse<Order> placeOrder(PlaceOrderRequest request){
        //参数检查，使用preconditions进行快速失败
        Preconditions.checkNotNull(request);
        //由于用户id 和产品id 不会变,定义成final
        final Integer userId=Preconditions.checkNotNull(request.getUserId());
        final Integer productId=Preconditions.checkNotNull(request.getProductId());
        Preconditions.checkState(request.getNum()>0);

        //通过产品id 获得产品
        Product product=findRemoteProduct(productId);
        if(Objects.isNull(product)){
            throw new GlobalException("找不到产品信息");
        }
        //获得用户信息
        User user=findRemoteUser(userId);
        if(Objects.isNull(user)){
            throw new GlobalException("找不到用户信息");
        }
        Date startDay=new Date();
        startDay.setTime(0);
        //构建订单
        Order order=new Order(new Date(),startDay,startDay,userId,productId,product.getPrice(),OrderStatus.PROCESSING,request.getNum());
        orderMapper.addOrder(order);
        //预留余额
        reserveBalanceAndPersistParticipant(order);
        //预留库存
        reserveProductAndPersistParticipant(order);
        return new ObjectDataResponse<>(order);
    }

    /**
     * 提交资源，确认订单，第一个c
     * @param request
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = RuntimeException.class)
    public ObjectDataResponse<Order> confirm(PaymentRequest request) {
        Preconditions.checkNotNull(request);
        Integer orderId=request.getOrderId();
        //检查订单是否存在
        Order order=orderMapper.getOrderById(orderId);
        if(Objects.isNull(order)){
            throw new GlobalException("订单不存在", StatusCode.Data_Not_Exist);
        }
        List<OrderParticipant> lop=orderParticipantMapper.listOrderParticipantByOrderId(orderId);
        if(lop.isEmpty()){
            throw new GlobalException("没有预留资源", StatusCode.Data_Not_Exist);
        }
        if(order.getStatus()== OrderStatus.PROCESSING){
            confirmPhase(order,lop);
        }
        return new ObjectDataResponse<>(order);
    }

    @Override
    public List<Order> listOrder() {
        return orderMapper.listOrder();
    }


    /**
     * 调用产品项目获得产品
     * @param productId
     * @return
     */
    private Product findRemoteProduct(Integer productId){
        try {
            return productService.getProductById(productId);
        }catch (Exception e) {
            logger.error("参数错误,找不到产品信息");
        }
        return null;
    }

    /**
     * 调用用户项目中获得用户
     * @param userId
     * @return
     */
    private User findRemoteUser(Integer userId){
        try {
           return userService.getUserById(userId);
        }catch (Exception e){
            logger.error("获得用户信息错误");
        }
        return null;
    }

    /**
     * 预留库存
     * @param order
     */
    private void reserveProductAndPersistParticipant(Order order){
        Preconditions.checkNotNull(order);
        try{
            productStockTccService.trying(order.getProductId(),order.getNum());
        }catch (Exception e){
            throw new GlobalException("预留库存失败！");
        }
    }

    /**
     * 预留余额
     * @param order
     */
    private void reserveBalanceAndPersistParticipant(Order order){
        Preconditions.checkNotNull(order);
        try {
            userBalanceTccService.trying(order.getUserId(), order.getPrice());
        }catch (Exception e){
            throw new GlobalException("预留余额失败！");
        }
    }

    /**
     * 添加资源信息
     * @param participant
     * @param orderId
     */
    private void persistParticipant(Participant participant, Integer orderId) {
        Preconditions.checkNotNull(participant);
        Preconditions.checkNotNull(orderId);
        OffsetDateTime defaultDateTime=OffsetDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.ofHours(8));
        OrderParticipant orderParticipant=new OrderParticipant(OffsetDateTime.now(),defaultDateTime,defaultDateTime,participant.getExpireTime(),participant.getUri(),orderId);
        orderParticipantMapper.addOrderParticipant(orderParticipant);
    }

    /**
     * 确认资源
     * @param order
     * @param lop
     */
    private void confirmPhase(Order order,List<OrderParticipant> lop){
        //guava 不可变合集,它是线程安全的
        ImmutableList<OrderParticipant> links=ImmutableList.copyOf(lop);
        TccRequest tccRequest=TccRequest.builder().orderParticipants(links).build();
        try {
            tccService.confirm(tccRequest);
            order.setStatus(OrderStatus.DONE);
            orderMapper.updateOrder(order);
        }catch (RuntimeException e){
            final Class<? extends Throwable> exceptionCause=e.getCause().getClass();
            if (ReservationExpireException.class.isAssignableFrom(exceptionCause)) {
                // 全部确认预留超时
                order.setStatus(OrderStatus.TIMEOUT);
                orderMapper.updateOrder(order);
                throw new GlobalException("预留超时:"+order.toString());
            } else if (PartialConfirmException.class.isAssignableFrom(exceptionCause)) {
                order.setStatus(OrderStatus.CONFLICT);
                orderMapper.updateOrder(order);
                markdownConfliction(order, e);
            } else {
                throw new GlobalException(e.getMessage());
            }
        }
    }

    /**
     * 标记冲突,存入mongodb
     * @param order
     * @param e
     */
    private void markdownConfliction(Order order, RuntimeException e) {
        Preconditions.checkNotNull(order);
        Preconditions.checkNotNull(e);
        final String message=e.getCause().getMessage();
        logger.error("order id "+order.getId()+" has come across an confliction. "+ message);
        // 错误信息保存到mongodb
        ErrorInfo errorInfo=new ErrorInfo<>(StatusCode.PartialConfirmerror,message,null,order,OffsetDateTime.now());
        errorRepository.insert(errorInfo);
        throw new GlobalException("order id "+order.getId()+" has come across an confliction. "+ message);

    }

    /**
     * 取消订单
     * @param request
     * @return
     */
    @Override
    public ObjectDataResponse<Order> cancel(CancelRequest request) {
        Preconditions.checkNotNull(request);
        final Integer orderId=request.getOrderId();
        //判断是否有这个订单
        final Order order=orderMapper.getOrderById(orderId);
        if(Objects.isNull(order)){
            throw new GlobalException("订单不存在",StatusCode.Data_Not_Exist);
        }
        List<OrderParticipant> lop=orderParticipantMapper.listOrderParticipantByOrderId(orderId);
        if(lop.isEmpty()){
            throw new GlobalException("没有预留资源",StatusCode.Data_Not_Exist);
        }
        if(order.getStatus()==OrderStatus.PROCESSING){
            cancelPhase(order,lop);
        }
        return new ObjectDataResponse<>(order);
    }

    /**
     * 取消资源
     * @param order
     * @param lop
     */
    private void cancelPhase(Order order,List<OrderParticipant> lop){
        ImmutableList<OrderParticipant> links=ImmutableList.copyOf(lop);
        TccRequest tccRequest=TccRequest.builder().orderParticipants(links).build();
        try{
            tccService.cancel(tccRequest);
            order.setStatus(OrderStatus.DONE);
            orderMapper.updateOrder(order);
        }catch (RuntimeException e){
            final Class<? extends Throwable> exceptionCause = e.getCause().getClass();
            if (ReservationExpireException.class.isAssignableFrom(exceptionCause)) {
                // 全部确认预留超时
                order.setStatus(OrderStatus.TIMEOUT);
                orderMapper.updateOrder(order);
            } else if (PartialConfirmException.class.isAssignableFrom(exceptionCause)) {
                order.setStatus(OrderStatus.DONE);
                orderMapper.updateOrder(order);
                markdownConfliction(order, e);
            } else {
                throw e;
            }
        }
    }
}
