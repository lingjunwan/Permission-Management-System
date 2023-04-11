package com.project.system.test;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.project.model.system.SysRole;
import com.project.system.service.SysRoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;

import java.util.List;


@SpringBootTest
public class SysRoleServiceTest {

    @Autowired
    private SysRoleService sysRoleService;

    //query all
    @Test
    public void findAll(){
        List<SysRole> list = sysRoleService.list();
        System.out.println(list);
    }

    //add
    @Test
    public void add(){
        SysRole sysRole = new SysRole();
        sysRole.setRoleName("Manager");
        sysRole.setRoleCode("role");
        sysRole.setDescription("Admin Manager");
        sysRoleService.save(sysRole);
    }

    //update
    @Test
    public void update(){
        SysRole sysRole = sysRoleService.getById(9);
        sysRole.setDescription("test");
        sysRoleService.updateById(sysRole);

    }

    //delete
    @Test
    public void remove(){
        sysRoleService.removeById(9);
    }

    //conditional query
    @Test
    public void select(){
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.eq("role_code","SYSTEM");
        List<SysRole> list = sysRoleService.list(wrapper);
        System.out.println(list);
    }
}
