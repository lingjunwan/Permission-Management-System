package com.project.system.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.project.model.system.SysUser;
import com.project.model.vo.SysUserQueryVo;

import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author lingjun
 * @since 2023-04-09
 */
public interface SysUserService extends IService<SysUser> {
    //User List
    IPage<SysUser> selectPage(Page<SysUser> pageParam, SysUserQueryVo sysUserQueryVo);

    //Update User Status
    void updateStatus(String id, Integer status);

    //Username query
    SysUser getUserInfoByUserName(String username);

    //Get user information (basic information and menu permission and button permission data) based on username
    Map<String, Object> getUserInfo(String username);
}
