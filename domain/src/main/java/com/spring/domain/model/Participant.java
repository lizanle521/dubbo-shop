package com.spring.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.spring.common.model.util.converter.OffsetDateTimeToIso8601Serializer;
import com.spring.domain.type.TccStatus;
import lombok.*;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * @author Zhao Junjian
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class Participant implements Serializable {

    private static final long serialVersionUID = -8013238614014494468L;

    private String uri;

    @JsonSerialize(using = OffsetDateTimeToIso8601Serializer.class)
    private OffsetDateTime expireTime;

    @JsonSerialize(using = OffsetDateTimeToIso8601Serializer.class)
    private OffsetDateTime executeTime;

    private ResponseEntity<?> participantErrorResponse;

    private TccStatus tccStatus = TccStatus.TO_BE_CONFIRMED;


    public Participant(String uri,OffsetDateTime executeTime){
        this.uri=uri;
        this.executeTime=executeTime;
    }

}
