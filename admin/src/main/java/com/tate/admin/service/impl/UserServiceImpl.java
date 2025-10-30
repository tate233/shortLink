package com.tate.admin.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tate.admin.common.convention.exception.ClientException;
import com.tate.admin.common.convention.result.Result;
import com.tate.admin.common.convention.result.Results;
import com.tate.admin.common.enums.UserErrorCodeEnum;
import com.tate.admin.dao.entity.UserDO;
import com.tate.admin.dao.mapper.UserMapper;
import com.tate.admin.dto.req.UserLoginReqDTO;
import com.tate.admin.dto.req.UserRegisterReqDTO;
import com.tate.admin.dto.req.UserUpdateReqDTO;
import com.tate.admin.dto.resp.UserLoginRespDTO;
import com.tate.admin.dto.resp.UserRespDTO;
import com.tate.admin.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private StringRedisTemplate stringRedisTemplate;



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

    /**
     * 根据用户名修改用户
     * @param requestParam
     */
    @Override
    public void update(UserUpdateReqDTO requestParam) {
        //TODO 验证当前用户名是否为登录用户 要搞网关那里再弄
        LambdaUpdateWrapper<UserDO> lambdaUpdateWrapper = Wrappers.lambdaUpdate(UserDO.class)
                .eq(UserDO::getUsername, requestParam.getUsername());
        baseMapper.update(BeanUtil.toBean(requestParam,UserDO.class),lambdaUpdateWrapper);
    }

    /**
     * 用户登录
     * @param requestParam
     * @return
     */
    @Override
    public UserLoginRespDTO login(UserLoginReqDTO requestParam) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, requestParam.getUsername())
                .eq(UserDO::getPassword, requestParam.getPassword())
                .eq(UserDO::getDelFlag,0) ;
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        if(userDO == null){
            throw  new ClientException(UserErrorCodeEnum.USER_NULL);
        }
        Boolean hasLogin = stringRedisTemplate.hasKey("login_" + requestParam.getUsername());
        if(hasLogin !=null && hasLogin){
            throw new ClientException("用户已登录");
        }
        /**
         * 这个有Bug是可以重复登录，改一下
         * Hash
         * Key:Login_用户名
         * Value:
         *  key:token
         *  val:JSON用户信息
         */
        String uuid = UUID.randomUUID().toString();
        stringRedisTemplate.opsForHash().put("login_" + requestParam.getUsername(),uuid,JSON.toJSONString(userDO));
        stringRedisTemplate.expire("login_"+requestParam.getUsername(),30L, TimeUnit.MINUTES);
        return new UserLoginRespDTO(uuid);
    }

    /**
     * 检查用户是否登录
     * @param token
     * @return
     */
    @Override
    public Boolean checkLogin(String username ,String token) {
        Object token1 = stringRedisTemplate.opsForHash().get("login_" + username, token);
        if(token1 !=null){
            return true;
        }
        return false;
    }

    /**
     * 退出登录
     * @param username
     * @param token
     * @return
     */
    @Override
    public Result<Void> logout(String username, String token) {
        if(checkLogin(username,token)){
            stringRedisTemplate.delete("login_"+username);
            return Results.success();
        }
        throw new ClientException("token不存在或用户未登录");
    }
}
