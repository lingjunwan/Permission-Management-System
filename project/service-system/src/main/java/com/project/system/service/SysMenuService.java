package com.project.system.service;

import com.project.model.system.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.project.model.vo.AssginMenuVo;
import com.project.model.vo.RouterVo;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author lingjun
 * @since 2023-04-11
 */
public interface SysMenuService extends IService<SysMenu> {
    //Menu list
    List<SysMenu> findNodes();

    //Delete Menu
    void removeMenuById(String id);

    //Change Menu Status
    void updateStatus(String id, Integer status);

    //Assign menus according to the role
    List<SysMenu> findMenuByRoleId(String roleId);

    //Assign menu permissions to roles
    void doAssign(AssginMenuVo assginMenuVo);

    //Query menu permission values based on userid
    List<RouterVo> getUserMenuList(String id);

    //Query button permission values based on userid
    List<String> getUserButtonList(String id);
}
