package com.spring.domain.request;

import com.spring.common.model.request.RestfulRequest;
import lombok.*;

import javax.validation.constraints.Min;

/**
 * @Description 生成预订单请求
 * @Author ErnestCheng
 * @Date 2017/5/31.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class PlaceOrderRequest extends RestfulRequest {

    @NonNull
    @Min(1)
    private Integer productId;

    @NonNull
    @Min(1)
    private Integer userId;

    @NonNull
    @Min(1)
    private Integer num;

}
