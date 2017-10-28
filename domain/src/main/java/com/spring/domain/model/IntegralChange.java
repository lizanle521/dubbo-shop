package com.spring.domain.model;

import com.spring.domain.type.IntegralChangeStatus;
import com.spring.domain.type.IntegralChangeType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

/**
 * 积分改变
 * @author cs
 * @date 2017/10/11
 */
@Data
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class IntegralChange {
    /**
     * 积分改变id
     */
    private int id;
    /**
     * 改变名称
     */
    private String changeName;
    /**
     * 规则编码
     */
    private String code;
    /**
     * 描述
     */
    private String changeDep;
    /**
     * 创建时间
     */
    private Timestamp createTime;
    /**
     * 积分变化类型
     */
    private IntegralChangeType changeType;
    /**
     * 状态
     */
    private IntegralChangeStatus status;
    /**
     * 更新时间
     */
    private Timestamp updateTime;
    /**
     * 数学公式
     */
    private String math;


}
