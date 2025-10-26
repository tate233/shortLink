package com.tate.admin.dao.entity;

//DO的意思是数据库持久层框架，其实就是我之前用的普通entity

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_user")
public class UserDO {
    /**
     * @description t_user
     * @author www.itgongju.com
     * @date 2025-10-19
     */
        private static final long serialVersionUID = 1L;

        /**
         * 用户名
         */
        private String username;

        /**
         * password
         */
        private String password;

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
        private String phone;

        /**
         * 邮箱
         */
        private String mail;

        /**
         * 注销时间戳
         */
        private Long deletionTime;

        /**
         * 创建时间
         */
        @TableField(fill = FieldFill.INSERT)
        private Date createTime;

        /**
         * 修改时间
         */
        @TableField(fill = FieldFill.INSERT_UPDATE)
        private Date updateTime;

        /**
         * 删除标识0：未删除1：已删除
         */
        @TableField(fill = FieldFill.INSERT)
        private int delFlag;



}
