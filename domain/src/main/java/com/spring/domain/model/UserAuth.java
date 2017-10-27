package com.spring.domain.model;

import lombok.Data;


import java.util.Date;
import java.util.List;

/**
 * @Description 用户权限对象
 * @Author ErnestCheng
 * @Date 2017/6/22.
 */
@Data
public class UserAuth {

    private String id;
    private  String userName;

    private String password;

    private List<String> roles;

    private Date lastPasswordResetDate;

}
