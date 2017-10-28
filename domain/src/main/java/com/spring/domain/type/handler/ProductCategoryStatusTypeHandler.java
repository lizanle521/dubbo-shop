package com.spring.domain.type.handler;


import com.spring.common.model.type.handler.GenericTypeHandler;
import com.spring.domain.type.ProductCategoryStatus;

/**
 * @author Mr.Cheng
 */
public class ProductCategoryStatusTypeHandler extends GenericTypeHandler<ProductCategoryStatus> {
    @Override
    public int getEnumIntegerValue(ProductCategoryStatus parameter) {
        return parameter.getStatus();
    }
}
