package com.tate.admin.common.biz.user;

import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Lists;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class UserTransmitFilter implements Filter {
    private final StringRedisTemplate stringRedisTemplate;

    private static final List<String> IGNORE_URI = Lists.newArrayList(
            "/api/short-link/v1/user/login",
            "/api/short-link/v1/user/check-login"
    );
    @Override
    @SneakyThrows
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {


        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String username = httpServletRequest.getHeader("username");
        String token = httpServletRequest.getHeader("token");
        String requestURI = httpServletRequest.getRequestURI();
        if ("/api/short-link/v1/user/login".equals(requestURI)) {
            filterChain.doFilter(servletRequest, servletResponse); // 直接放行
            return; // 结束
        }
            Object userInfoJsonStr = stringRedisTemplate.opsForHash().get("login_" + username, token);
            if (userInfoJsonStr != null) {
                UserInfoDTO userInfoDTO = JSON.parseObject(userInfoJsonStr.toString(), UserInfoDTO.class);
                UserContext.setUser(userInfoDTO);
                log.info("过滤器的username:{}",username);
            }else{
                log.info("token 失效");
            }

//            String token = httpServletRequest.getHeader(UserConstant.USER_TOKEN_KEY);
//            UserInfoDTO userInfoDTO = UserInfoDTO.builder()
//                    .userId(userId)
//                    .username(userName)
//                    .realName(realName)
//                    .token(token)
//                    .build();
//            UserContext.setUser(userInfoDTO);

            try {
                filterChain.doFilter(servletRequest, servletResponse);
            } finally {
                UserContext.removeUser();
        }
    }
}
