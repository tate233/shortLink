package com.tate.admin.dto.resp;

import lombok.Data;

/**
 * 用户返回参数实体
 */
@Data
public class UserActualRespDTO {

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * id
     */
    private Long id;

    /**
     * 手机号
     */
   //@JsonSerialize(using = PhoneDesensitizationSerializer.class)
    private String phone;

    /**
     * 邮箱
     */
    private String mail;


}
