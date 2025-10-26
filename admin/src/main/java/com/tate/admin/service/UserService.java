package com.tate.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tate.admin.dao.entity.UserDO;
import com.tate.admin.dto.resp.UserRespDTO;

/**
 * 用户接口层
 */
public interface UserService extends IService<UserDO> {
    UserRespDTO getUserByUsername(String username);
}
