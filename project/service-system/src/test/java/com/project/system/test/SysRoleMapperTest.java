package com.project.system.test;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.project.model.system.SysRole;
import com.project.system.mapper.SysRoleMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class SysRoleMapperTest {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    //7. Conditional delete
    @Test
    public void testDelete(){
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.like("role_name","test");
        sysRoleMapper.delete(wrapper);
    }
    //6. Conditional Query
    @Test
    public void testSelect(){
        //Creating a conditional constructor object
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        //Set conditions
        //wrapper.eq("role_name","test");
        wrapper.like("role_name","test");
        //Calling Methods
        List<SysRole> list = sysRoleMapper.selectList(wrapper);
        System.out.println(list);
    }

    //5. Batch delete
    @Test
    public void testBatchDelete(){
        sysRoleMapper.deleteBatchIds(Arrays.asList(1,2));
    }

    //4. Delete by id
    @Test
    public void deleteId(){
        int rows = sysRoleMapper.deleteById(9);
    }

    //3. Uodate
    @Test
    public void update(){
        //find by id
        SysRole sysRole = sysRoleMapper.selectById(9);

        //Set the modification value
        sysRole.setDescription("system admin");

        //Calling Methods
        sysRoleMapper.updateById(sysRole);
    }

    //2. Add
    @Test
    public void add(){
        SysRole sysRole = new SysRole();
        sysRole.setRoleName("test role1");
        sysRole.setRoleCode("testManager1");
        sysRole.setDescription("test role1");
        int rows = sysRoleMapper.insert(sysRole);
        System.out.println(rows);
    }

    //1. query all records of table
    @Test
    public void findAll(){
        List<SysRole> list = sysRoleMapper.selectList(null);
        for(SysRole sysRole:list){
            System.out.println(sysRole);
        }
    }
}
