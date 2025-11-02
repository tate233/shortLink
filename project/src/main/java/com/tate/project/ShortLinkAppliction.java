package com.tate.project;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.tate.project.dao.mapper")
public class ShortLinkAppliction {
        public static void main(String[] args) {
            SpringApplication.run(ShortLinkAppliction.class, args);
        }
}
