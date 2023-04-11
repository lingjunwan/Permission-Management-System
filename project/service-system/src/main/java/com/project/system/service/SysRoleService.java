package com.project.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.project.model.system.SysRole;
import com.project.model.vo.AssginRoleVo;
import com.project.model.vo.SysRoleQueryVo;

import java.util.Map;

public interface SysRoleService extends IService<SysRole> {
    //Conditional Paging Query
    IPage<SysRole> selectPage(Page<SysRole> pageParam, SysRoleQueryVo sysRoleQueryVo);

    //Get the user's role data
    Map<String, Object> getRolesByUserId(String userId);

    //Assigning roles to users
    void doAssign(AssginRoleVo assginRoleVo);
}
