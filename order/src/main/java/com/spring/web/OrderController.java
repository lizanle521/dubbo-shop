package com.spring.web;

import com.reger.dubbo.annotation.Inject;
import com.spring.common.model.response.ObjectDataResponse;

import com.spring.domain.model.Order;
import com.spring.domain.model.User;
import com.spring.domain.request.CancelRequest;
import com.spring.domain.request.PaymentRequest;
import com.spring.domain.request.PlaceOrderRequest;
import com.spring.service.OrderService;
import com.spring.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @Description 订单控制器
 * @author ErnestCheng
 * @Date 2017/5/31.
 */
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Inject
    private UserService userService;

    @ApiOperation(value="获得所有订单列表")
    @RequestMapping(value="listOrder",method = RequestMethod.GET)
    public ObjectDataResponse listOrder(){
        List<Order> lo=orderService.listOrder();
        return new ObjectDataResponse(lo);
    }

    /**
     * 预留资源
     * @return
     */
    @ApiOperation(value="下预订单")
    @RequestMapping(value="placeOrder",method = RequestMethod.POST)
    public ObjectDataResponse<Order> placeOrder(@Valid @RequestBody PlaceOrderRequest placeOrderRequest, BindingResult result){
        return orderService.placeOrder(placeOrderRequest);
    }

    @ApiOperation(value="支付订单")
    @RequestMapping(value="payOff",method = RequestMethod.POST)
    public ObjectDataResponse<Order> payOff(@Valid @RequestBody PaymentRequest paymentRequest, BindingResult result){
        return   orderService.confirm(paymentRequest);
    }

    @ApiOperation(value ="取消订单")
    @DeleteMapping(value="cancel")
    public ObjectDataResponse<Order> cancel(@Valid @RequestBody CancelRequest cancelRequest, BindingResult result){
        return orderService.cancel(cancelRequest);
    }

    @ApiOperation(value ="获得用户")
    @GetMapping("/user/{id}")
    public ObjectDataResponse<User> getUserById(@PathVariable("id") int id) throws InvocationTargetException, IntrospectionException, InstantiationException, IllegalAccessException {
        return new ObjectDataResponse<>(userService.getUserById(id));
    }


}
