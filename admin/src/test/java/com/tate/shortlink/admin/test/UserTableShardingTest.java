package com.tate.shortlink.admin.test;

public class UserTableShardingTest {
    public static final String SQL = "CREATE TABLE `t_user_%d` (\n" +
            "  `username` varchar(100) DEFAULT NULL COMMENT '用户名',\n" +
            "  `password` varchar(512) DEFAULT NULL,\n" +
            "  `real_name` varchar(256) DEFAULT NULL COMMENT '真实姓名',\n" +
            "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
            "  `phone` varchar(128) DEFAULT NULL COMMENT '手机号',\n" +
            "  `mail` varchar(512) DEFAULT NULL COMMENT '邮箱',\n" +
            "  `deletion_time` bigint(20) DEFAULT NULL COMMENT '注销时间戳',\n" +
            "  `create_time` datetime DEFAULT NULL COMMENT '创建时间',\n" +
            "  `update_time` datetime DEFAULT NULL COMMENT '修改时间',\n" +
            "  `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除标识0：未删除1：已删除',\n" +
            "  PRIMARY KEY (`id`),\n" +
            "  UNIQUE KEY `t_user_unique` (`username`)\n" +
            ") ENGINE=MyISAM AUTO_INCREMENT=1982454069642514434 DEFAULT CHARSET=utf8mb4;";
    public static void main(String[] args) {
        for(int i  = 0;i<16;i++){
            System.out.printf((SQL) + "%n",i);
        }
    }
}
