package com.spring.service;



import com.spring.domain.model.Product;
import com.spring.domain.request.ProductUpdateRequest;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

/**
 * @Description 产品service接口
 * @Author ErnestCheng
 * @Date 2017/5/27.
 */
public interface ProductService {
    /**
     * 添加产品
     * @param product
     */
    void addProduct(Product product);

    /**
     * 通过产品id获得产品
     * @param productId
     * @return
     */
    Product getProductById(Integer productId) throws IllegalAccessException, IntrospectionException, InvocationTargetException, InstantiationException;

    /**
     * 更新产品信息
     * @param productUpdateRequest
     * @return
     */
    int updateProduct(ProductUpdateRequest productUpdateRequest);

    /**
     * 删除产品
     * @param productId
     * @return
     */
    int deleteProductByProductId(Integer productId);
}
