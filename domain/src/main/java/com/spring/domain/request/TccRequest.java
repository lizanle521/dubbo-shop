package com.spring.domain.request;

import com.spring.common.model.request.RestfulRequest;

import com.spring.domain.model.OrderParticipant;
import com.spring.domain.model.Participant;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @Description 参与方提供的链接集合
 * @Author ErnestCheng
 * @Date 2017/6/2.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TccRequest extends RestfulRequest {

    @NotNull
    @Size(min=1)
    @Valid
    private List<Participant> participants;

}
