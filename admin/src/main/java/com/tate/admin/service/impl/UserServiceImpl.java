package com.tate.admin.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tate.admin.common.convention.exception.ClientException;
import com.tate.admin.common.enums.UserErrorCodeEnum;
import com.tate.admin.dao.entity.UserDO;
import com.tate.admin.dao.mapper.UserMapper;
import com.tate.admin.dto.resp.UserRespDTO;
import com.tate.admin.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 用户接口实现层
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {
    @Override
    public UserRespDTO getUserByUsername(String username) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, username);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        UserRespDTO userRespDTO = new UserRespDTO();
        if (userDO == null){
            throw new ClientException(UserErrorCodeEnum.USER_NULL);
        }
        BeanUtils.copyProperties(userDO,userRespDTO);
        log.info("根据用户名查询用户信息：{}",userRespDTO);
        return userRespDTO;
    }
}
