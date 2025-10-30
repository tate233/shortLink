package com.tate.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tate.admin.common.convention.result.Result;
import com.tate.admin.dao.entity.UserDO;
import com.tate.admin.dto.req.UserLoginReqDTO;
import com.tate.admin.dto.req.UserRegisterReqDTO;
import com.tate.admin.dto.req.UserUpdateReqDTO;
import com.tate.admin.dto.resp.UserLoginRespDTO;
import com.tate.admin.dto.resp.UserRespDTO;

/**
 * 用户接口层
 */
public interface UserService extends IService<UserDO> {
    UserRespDTO getUserByUsername(String username);

    /**
     * 查询用户名是否存在
     * @param username
     * @return
     */
    Boolean hasUsername(String username);

    void Register(UserRegisterReqDTO requestParam);

    void update(UserUpdateReqDTO requestParam);

    UserLoginRespDTO login(UserLoginReqDTO requestParam);

    Boolean checkLogin(String username,String token);

    Result<Void> logout(String username, String token);
}
