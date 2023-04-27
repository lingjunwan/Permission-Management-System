package com.project.system.filter;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.common.result.Result;
import com.project.common.result.ResultCodeEnum;
import com.project.common.utils.JwtHelper;
import com.project.common.utils.ResponseUtil;
import com.project.model.vo.LoginVo;
import com.project.system.custom.CustomUser;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lingjun
 * @date 2023/4/26 12:39
 */

//Authentication filter, login verification for username and password
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {
    private RedisTemplate redisTemplate;
    //Construct
    public TokenLoginFilter(AuthenticationManager authenticationManager, RedisTemplate redisTemplate) {
        this.setAuthenticationManager(authenticationManager);
        this.setPostOnly(false);
        //Specify the login interface and submission method, you can specify any path
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/admin/system/index/login","POST"));
        this.redisTemplate = redisTemplate;
    }

    //Get username & password for authentication
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse res)
            throws AuthenticationException {
        try {
            LoginVo loginVo = new ObjectMapper().readValue(request.getInputStream(), LoginVo.class);

            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(loginVo.getUsername(), loginVo.getPassword());
            return this.getAuthenticationManager().authenticate(authenticationToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
        //Get certified objects
        CustomUser customUser = (CustomUser) auth.getPrincipal();

        //Save permission data
        redisTemplate.opsForValue().set(customUser.getUsername(), JSON.toJSONString(customUser.getAuthorities()));

        //Generate Token
        String token = JwtHelper.createToken(customUser.getSysUser().getId(), customUser.getSysUser().getUsername());

        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        ResponseUtil.out(response, Result.ok(map));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException e) throws IOException, ServletException {

        if(e.getCause() instanceof RuntimeException) {
            ResponseUtil.out(response, Result.build(null, 204, e.getMessage()));
        } else {
            ResponseUtil.out(response, Result.build(null, ResultCodeEnum.LOGIN_MOBLE_ERROR));
        }
    }
}
