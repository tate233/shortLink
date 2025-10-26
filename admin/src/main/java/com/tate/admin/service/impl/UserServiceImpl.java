package com.tate.admin.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tate.admin.common.convention.exception.ClientException;
import com.tate.admin.common.enums.UserErrorCodeEnum;
import com.tate.admin.dao.entity.UserDO;
import com.tate.admin.dao.mapper.UserMapper;
import com.tate.admin.dto.req.UserRegisterReqDTO;
import com.tate.admin.dto.resp.UserRespDTO;
import com.tate.admin.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.tate.admin.common.constant.RedisCacheConstant.LOCK_USER_REGISTER;

/**
 * 用户接口实现层
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RBloomFilter<String> userRegisterCachePenetrationBloomFilter;

    @Autowired
    private RedissonClient redissonClient;
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

    @Override
    public Boolean hasUsername(String username) {
        return userRegisterCachePenetrationBloomFilter.contains(username);
    }

    @Override
    public void Register(UserRegisterReqDTO requestParam) {
        if(hasUsername(requestParam.getUsername())){
            throw new ClientException(UserErrorCodeEnum.USER_NAME_EXIST);
        }
        RLock lock = redissonClient.getLock(LOCK_USER_REGISTER + requestParam.getUsername());
        try {
            if (lock.tryLock()) {
                int inserted = baseMapper.insert(BeanUtil.toBean(requestParam, UserDO.class));
                if (inserted < 1) {
                    throw new ClientException(UserErrorCodeEnum.USER_SAVE_ERROR);
                }
                userRegisterCachePenetrationBloomFilter.add(requestParam.getUsername());
                return;
            } throw new ClientException(UserErrorCodeEnum.USER_NAME_EXIST);
        }finally {
            lock.unlock();
        }
    }
}
