package com.spring.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.spring.common.model.util.converter.OffsetDateTimeToIso8601Serializer;
import lombok.*;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * @author Zhao Junjian
 */
@Getter
@Setter
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
}
