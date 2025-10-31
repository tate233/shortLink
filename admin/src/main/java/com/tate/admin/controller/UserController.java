package com.tate.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import com.tate.admin.common.convention.result.Result;
import com.tate.admin.common.convention.result.Results;
import com.tate.admin.dto.req.UserLoginReqDTO;
import com.tate.admin.dto.req.UserRegisterReqDTO;
import com.tate.admin.dto.req.UserUpdateReqDTO;
import com.tate.admin.dto.resp.UserActualRespDTO;
import com.tate.admin.dto.resp.UserLoginRespDTO;
import com.tate.admin.dto.resp.UserRespDTO;
import com.tate.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制层
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/api/short-link/v1/user/{username}")
    public Result<UserRespDTO> getUserByUsername(@PathVariable ("username") String username){
        UserRespDTO userByUsername = userService.getUserByUsername(username);
        return Results.success(userByUsername);
    }

    @GetMapping("/api/short-link/v1/actual/user/{username}")
    public Result<UserActualRespDTO> getActualUserByUsername(@PathVariable ("username") String username){
        UserRespDTO userByUsername = userService.getUserByUsername(username);
        return Results.success(BeanUtil.toBean(userByUsername,UserActualRespDTO.class));
    }

    @GetMapping("/api/short-link/v1/user/has-username")
    public Result<Boolean> hasUsername(@RequestParam("username") String username){
        return Results.success(userService.hasUsername(username));
    }

    /**
     * 用户注册
     */
    @PostMapping("/api/short-link/v1/user")
    public Result<Void> register(@RequestBody UserRegisterReqDTO requestParam){
        userService.Register(requestParam);
        return Results.success();
    }

    /**
     * 修改用户信息
     */
    @PutMapping("/api/short-link/v1/user")
    public Result<Void> update(@RequestBody UserUpdateReqDTO requestParam){
        userService.update(requestParam);
        return Results.success();
    }

    /**
     * 用户登录
     */
    @PostMapping("/api/short-link/v1/user/login")
    public Result<UserLoginRespDTO> login(@RequestBody UserLoginReqDTO requestParam){
        return Results.success(userService.login(requestParam));
    }

    /**
     * 检查用户是否登录
     */
    @GetMapping("/api/short-link/v1/user/check-login")
    public Result<Boolean> checkLogin(@RequestParam ("username") String username,
    @RequestParam ("token") String token){
        return Results.success(userService.checkLogin(username,token));
    }

    /**
     * 退出登录
     */
    @DeleteMapping("/api/short-link/v1/user/logout")
    public Result<Void> logout(@RequestParam("username")String username,
                               @RequestParam("token")String token){
        return userService.logout(username,token);
    }
}
