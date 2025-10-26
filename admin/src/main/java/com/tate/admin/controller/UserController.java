package com.tate.admin.controller;

import com.tate.admin.common.convention.result.Result;
import com.tate.admin.common.convention.result.Results;
import com.tate.admin.common.enums.UserErrorCodeEnum;
import com.tate.admin.dto.resp.UserRespDTO;
import com.tate.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户管理控制层
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("api/shortlink/v1/user/{username}")
    public Result<UserRespDTO> getUserByUsername(@PathVariable ("username") String username){
        UserRespDTO userByUsername = userService.getUserByUsername(username);
        if(userByUsername == null){
            return new Result<UserRespDTO>().setCode(UserErrorCodeEnum.USER_NULL.code()).setMessage(UserErrorCodeEnum.USER_NULL.message());
        }else{
            return Results.success(userByUsername);
        }
    }

}
