package com.spring.domain.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.spring.common.model.request.RestfulRequest;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author Zhao Junjian
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class BalanceReservationRequest extends RestfulRequest {

    private static final long serialVersionUID = -7315046960598963993L;

    @NotNull
    @Min(1)
    private Integer userId;

    @NotNull
    @Min(1)
    @Max(100000000L)
    private BigDecimal amount;

}
