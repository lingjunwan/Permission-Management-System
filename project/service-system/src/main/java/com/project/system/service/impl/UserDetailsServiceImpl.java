package com.project.system.service.impl;

import com.project.model.system.SysUser;
import com.project.system.custom.CustomUser;
import com.project.system.service.SysMenuService;
import com.project.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Lingjun
 * @date 2023/4/25 23:57
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysMenuService sysMenuService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.getUserInfoByUserName(username);
        if(null == sysUser) {
            throw new UsernameNotFoundException("Username does not exist!");
        }

        if(sysUser.getStatus().intValue() == 0) {
            throw new RuntimeException("Account is deactivated.");
        }
        //Query operation permission data based on userid
        List<String> userPermsList = sysMenuService.getUserButtonList(sysUser.getId());
        //Convert data to security required format
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (String perm : userPermsList) {
            authorities.add(new SimpleGrantedAuthority(perm.trim()));
        }
        return new CustomUser(sysUser, authorities);
    }
}
