package com.spring.domain.request;

import com.spring.common.model.request.RestfulRequest;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Description 支付订单
 * @Author ErnestCheng
 * @Date 2017/6/2.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentRequest extends RestfulRequest {

    @Min(1)
    @NotNull
    private Integer orderId;
}
