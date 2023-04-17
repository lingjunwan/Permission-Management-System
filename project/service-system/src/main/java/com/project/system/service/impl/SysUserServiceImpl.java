package com.project.system.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.model.system.SysUser;
import com.project.model.vo.RouterVo;
import com.project.model.vo.SysUserQueryVo;
import com.project.system.mapper.SysUserMapper;
import com.project.system.service.SysMenuService;
import com.project.system.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author lingjun
 * @since 2023-04-09
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysMenuService sysMenuService;

    //User List
    @Override
    public IPage<SysUser> selectPage(Page<SysUser> pageParam, SysUserQueryVo sysUserQueryVo) {
        return baseMapper.selectPage(pageParam,sysUserQueryVo);
    }

    //Update User Status
    @Override
    public void updateStatus(String id, Integer status) {
        //Search by user id
        SysUser sysUser = baseMapper.selectById(id);
        //Set update status
        sysUser.setStatus(status);
        //Call method to update
        baseMapper.updateById(sysUser);
    }

    //Username query
    @Override
    public SysUser getUserInfoByUserName(String username) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        return baseMapper.selectOne(wrapper);
    }

    //Get user information (basic information and menu permission and button permission data) based on username
    @Override
    public Map<String, Object> getUserInfo(String username) {
        //Query user basic information according to username
        SysUser sysUser = this.getUserInfoByUserName(username);
        //Query menu permission values based on userid
        List<RouterVo> routerVolist = sysMenuService.getUserMenuList(sysUser.getId());
        //Query button permission values based on userid
        List<String> permsList = sysMenuService.getUserButtonList(sysUser.getId());

        Map<String,Object> result = new HashMap<>();
        result.put("name",username);
        result.put("avatar","https://giffiles.alphacoders.com/398/3987.gif");
        result.put("roles","[\"admin\"]");
        //Menu permission data
        result.put("routers",routerVolist);
        //Button permission data
        result.put("buttons",permsList);
        return result;
    }
}
