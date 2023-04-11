package com.project.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.model.system.SysRole;
import com.project.model.system.SysUserRole;
import com.project.model.vo.AssginRoleVo;
import com.project.model.vo.SysRoleQueryVo;
import com.project.system.mapper.SysRoleMapper;
import com.project.system.mapper.SysUserRoleMapper;
import com.project.system.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysRoleServiceImpl extends ServiceImpl <SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    //Conditional Paging Query
    @Override
    public IPage<SysRole> selectPage(Page<SysRole> pageParam, SysRoleQueryVo sysRoleQueryVo) {
        IPage<SysRole> pageModel = baseMapper.selectPage(pageParam,sysRoleQueryVo);
        return pageModel;
    }

    //Get the user's role data
    @Override
    public Map<String, Object> getRolesByUserId(String userId) {
        // Get all roles
        List<SysRole> roles = baseMapper.selectList(null);
        // Query the assigned role information based on the user id
        QueryWrapper<SysUserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        List<SysUserRole>userRolesList = sysUserRoleMapper.selectList(wrapper);
        // Get all role ids from the userRoles collection
        List<String> userRoleIds = new ArrayList<>();
        for(SysUserRole userRole:userRolesList){
            String roleId = userRole.getRoleId();
            userRoleIds.add(roleId);
        }
        // Wrapping to a map collection
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("allRoles",roles);
        returnMap.put("userRoleIds",userRoleIds);
        return returnMap;
    }

    //Assigning roles to users
    @Override
    public void doAssign(AssginRoleVo assginRoleVo) {
        //Delete previously assigned roles based on user id
        QueryWrapper<SysUserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",assginRoleVo.getUserId());
        sysUserRoleMapper.delete(wrapper);

        //Get all role ids and add them to the role-user relationship table
        List<String> roleIdList = assginRoleVo.getRoleIdList();
        for (String roleId : roleIdList) {
                SysUserRole userRole = new SysUserRole();
                userRole.setUserId(assginRoleVo.getUserId());
                userRole.setRoleId(roleId);
                sysUserRoleMapper.insert(userRole);
            }
        }

    }

