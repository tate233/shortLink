package com.tate.admin.dao.entity;

//DO的意思是数据库持久层框架，其实就是我之前用的普通entity

import com.baomidou.mybatisplus.annotation.TableName;
import com.tate.admin.common.database.BaseDO;
import lombok.Data;

@Data
@TableName("t_user")
public class UserDO extends BaseDO {
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




}
