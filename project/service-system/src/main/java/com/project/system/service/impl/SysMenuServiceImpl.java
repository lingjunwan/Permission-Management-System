package com.project.system.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.project.model.system.SysRoleMenu;
import com.project.model.vo.AssginMenuVo;
import com.project.model.vo.RouterVo;
import com.project.system.exception.CustomException;
import com.project.system.mapper.SysRoleMenuMapper;
import com.project.system.utils.MenuHelper;
import com.project.model.system.SysMenu;
import com.project.system.mapper.SysMenuMapper;
import com.project.system.service.SysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.system.utils.RouterHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author lingjun
 * @since 2023-04-11
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public List<SysMenu> findNodes() {
        //Get all menus
        List<SysMenu> sysMenuList = baseMapper.selectList(null);
        //Convert all menu data to the required data format
        List<SysMenu> resultList = MenuHelper.buildTree(sysMenuList);
        return resultList;
    }

    @Override
    public void removeMenuById(String id) {
        // Query if there are submenus under the current delete menu
        // Based on the correspondence between id and parentid
        QueryWrapper<SysMenu> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        Integer count = baseMapper.selectCount(wrapper);
        if(count > 0) {//With Submenu
            throw new CustomException(201,"Please delete the submenu first");
        }
        //Call to remove
        baseMapper.deleteById(id);
    }

    //Change Menu Status
    @Override
    public void updateStatus(String id, Integer status) {
        //Search by menu id
        SysMenu sysMenu = baseMapper.selectById(id);
        //Set update status
        sysMenu.setStatus(status);
        //Call method to update
        baseMapper.updateById(sysMenu);
    }

    //Assign menus according to the role
    @Override
    public List<SysMenu> findMenuByRoleId(String roleId) {
        //Get all menus: status=1
        QueryWrapper<SysMenu> wrapperMenu = new QueryWrapper<>();
        wrapperMenu.eq("status",1);
        List<SysMenu> menuList = baseMapper.selectList(wrapperMenu);

        //Query the list of menus already assigned to a role based on the role id
        QueryWrapper<SysRoleMenu> wrapperRoleMenu = new QueryWrapper<>();
        wrapperRoleMenu.eq("role_id",roleId);
        List<SysRoleMenu> roleMenus = sysRoleMenuMapper.selectList(wrapperRoleMenu);

        //Get the ids of all menus assigned to the role from step 2
        List<String> roleMenuIds = new ArrayList<>();
        for (SysRoleMenu sysRoleMenu:roleMenus) {
            String menuId = sysRoleMenu.getMenuId();
            roleMenuIds.add(menuId);
        }
        //Data Processing: isSelect -- toggle menu status
        for (SysMenu sysMenu:menuList) {
            if(roleMenuIds.contains(sysMenu.getId())) {
                sysMenu.setSelect(true);
            } else {
                sysMenu.setSelect(false);
            }
        }
        //Convert to a tree structure for final display. MenuHelper method implementation
        List<SysMenu> sysMenus = MenuHelper.buildTree(menuList);
        return sysMenus;
    }

    //Assign menu permissions to roles
    @Override
    public void doAssign(AssginMenuVo assginMenuVo) {
        //Remove menu permissions based on role id
        QueryWrapper<SysRoleMenu> wrapper = new QueryWrapper<>();
        wrapper.eq("role_id",assginMenuVo.getRoleId());
        sysRoleMenuMapper.delete(wrapper);

        //Iterate through the list of menu ids and add them one by one
        List<String> menuIdList = assginMenuVo.getMenuIdList();
        for (String menuId:menuIdList) {
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setMenuId(menuId);
            sysRoleMenu.setRoleId(assginMenuVo.getRoleId());
            sysRoleMenuMapper.insert(sysRoleMenu);
        }
    }

    //Query menu permission values based on userid
    @Override
    public List<RouterVo> getUserMenuList(String userId) {
        //admin is the super administrator and operates all content
        List<SysMenu> sysMenuList = null;
        //Determine the userid value, 1 is for super administrator, query all permission data
        if("1".equals(userId)) {
            QueryWrapper<SysMenu> wrapper = new QueryWrapper<>();
            wrapper.eq("status",1);
            wrapper.orderByAsc("sort_value");
            sysMenuList = baseMapper.selectList(wrapper);
        } else {
            //If userid is not 1, then it is other type of user, query this user permission
            sysMenuList = baseMapper.findMenuListUserId(userId);
        }

        //tree structure
        List<SysMenu> sysMenuTreeList = MenuHelper.buildTree(sysMenuList);

        //Convert data into the format required by the front-end routing
        List<RouterVo> routerVoList = RouterHelper.buildRouters(sysMenuTreeList);
        return routerVoList;
    }

    //Query button permission values based on userid
    @Override
    public List<String> getUserButtonList(String userId) {
        List<SysMenu> sysMenuList = null;
        //check if the user is the admin
        if("1".equals(userId)){
            sysMenuList = baseMapper.selectList(new QueryWrapper<SysMenu>().eq("status", 1));
        } else {
            sysMenuList = baseMapper.findMenuListUserId(userId);
        }
        //Iterate through sysMenuList
        List<String> permissionList = new ArrayList<>();
        for (SysMenu sysMenu : sysMenuList) {
        // type=2 => button
            if (sysMenu.getType()==2){
                String perms = sysMenu.getPerms();
                permissionList.add(perms);
            }    
        }
            return permissionList;
        }
}

