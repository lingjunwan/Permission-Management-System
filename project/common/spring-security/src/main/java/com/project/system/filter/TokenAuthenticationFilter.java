package com.project.system.filter;

import com.alibaba.fastjson.JSON;
import com.project.common.result.Result;
import com.project.common.result.ResultCodeEnum;
import com.project.common.utils.JwtHelper;
import com.project.common.utils.ResponseUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Lingjun
 * @date 2023/4/26 14:34
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private RedisTemplate redisTemplate;

    public TokenAuthenticationFilter(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        logger.info("uri:"+request.getRequestURI());
        //If it's a login interface, let it go directly
        if("/admin/system/index/login".equals(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        if(null != authentication) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } else {
            ResponseUtil.out(response, Result.build(null, ResultCodeEnum.PERMISSION));
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        // The token is placed in the header
        String token = request.getHeader("token");
        logger.info("token:"+token);
        if (!StringUtils.isEmpty(token)) {
            String useruame = JwtHelper.getUsername(token);
            logger.info("useruame:"+useruame);
            if (!StringUtils.isEmpty(useruame)) {
                String authoritiesString = (String) redisTemplate.opsForValue().get(useruame);
                List<Map> mapList = JSON.parseArray(authoritiesString, Map.class);
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                for (Map map : mapList) {
                    authorities.add(new SimpleGrantedAuthority((String)map.get("authority")));
                }
                return new UsernamePasswordAuthenticationToken(useruame, null, authorities);
            }
        }
        return null;
    }
}
