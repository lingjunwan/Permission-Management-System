package com.project.system.utils;

import com.project.model.system.SysMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lingjun
 * @date 2023/4/11 11:56
 */
public class MenuHelper {

    public static List<SysMenu> buildTree(List<SysMenu> sysMenuList) {
        //Create collections to encapsulate the final data
        List<SysMenu> trees = new ArrayList<>();
        for (SysMenu sysMenu : sysMenuList) {
            //Find recursive entry ParentId==0
            if (sysMenu.getParentId().longValue() == 0) {
                trees.add(findChildren(sysMenu,sysMenuList));
            }
        }
        return trees;
    }

    //Recursive queries from the root node, querying the child nodes
    //Determine whether id and parentId are the same, if the same then id is a child node, perform data encapsulation
    private static SysMenu findChildren(SysMenu sysMenu, List<SysMenu> treeNodes) {
        sysMenu.setChildren(new ArrayList<SysMenu>());
        //Iterate recursive search
        for (SysMenu it:treeNodes) {
            if(Long.parseLong(sysMenu.getId())==it.getParentId()) {
//            if(sysMenu.getId().longValue() == it.getParentId().longValue()) {
                if(sysMenu.getChildren()==null) {
                    sysMenu.setChildren(new ArrayList<>());
                }
                sysMenu.getChildren().add(findChildren(it,treeNodes));
            }
        }
        return sysMenu;
    }
}

