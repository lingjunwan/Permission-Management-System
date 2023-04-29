package com.project.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.common.result.Result;
import com.project.model.system.SysRole;
import com.project.model.vo.AssginRoleVo;
import com.project.model.vo.SysRoleQueryVo;
import com.project.system.exception.CustomException;
import com.project.system.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@Api(tags = "角色管理接口")
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @ApiOperation(value = "获取用户的角色数据")
    @GetMapping("toAssign/{userId}")
    public Result toAssign(@PathVariable String userId) {
        Map<String, Object> roleMap = sysRoleService.getRolesByUserId(userId);
        return Result.ok(roleMap);
    }

    @ApiOperation(value = "用户分配角色")
    @PostMapping("doAssign")
    public Result doAssign(@RequestBody AssginRoleVo assginRoleVo) {
        sysRoleService.doAssign(assginRoleVo);
        return Result.ok();
    }

    //7.Batch Remove
    //multiple id value [1,2,3]
    //json array format -- java's list collection
    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @ApiOperation("批量删除")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> ids) {
        sysRoleService.removeByIds(ids);
        return Result.ok();
    }


    //6.Update-FinalUpdate
    @PreAuthorize("hasAuthority('bnt.sysRole.update')")
    @ApiOperation("最终修改")
    @PutMapping("update")
    public Result updateRole(@RequestBody SysRole sysRole){
        boolean isSuccess = sysRoleService.updateById(sysRole);
        if(isSuccess){
            return Result.ok();
        } else{
            return Result.fail();
        }
    }

    //5.Update-FindById
    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation("根据id查询")
    @PostMapping("findRoleById/{id}")
    public Result findRoleById(@PathVariable Long id) {
        SysRole sysRole = sysRoleService.getById(id);
        return Result.ok(sysRole);
    }

    //4.Add
    //@ResquestBody cannot be submitted using the @GetMapping
    //Pass json format data, wrap json format data into an object {...}
    @PreAuthorize("hasAuthority('bnt.sysRole.add')")
    @ApiOperation("添加角色")
    @PostMapping("save")
    public Result saveRole(@RequestBody SysRole sysRole) {
        boolean isSuccess = sysRoleService.save(sysRole);
        if(isSuccess){
            return Result.ok();
        } else{
            return Result.fail();
        }
    }


    //3.Conditional Paging Query
    // page for Current page, limit for Number of records per page
    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation("条件分页查询")
    @GetMapping("{page}/{limit}")
    public Result findPageQueryRole(@PathVariable Long page,
                                    @PathVariable Long limit,
                                    SysRoleQueryVo sysRoleQueryVo){
        //create object for page
        Page<SysRole> pageParam = new Page<>(page, limit);
        //Calling the service method
        IPage<SysRole> pageModel = sysRoleService.selectPage(pageParam, sysRoleQueryVo);
        //Return
        return Result.ok(pageModel);
    }


    //2.Logical Deletion interface
    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @ApiOperation("逻辑删除接口")
    @DeleteMapping("remove/{id}")
    public Result removeRole(@PathVariable Long id){
        //Call method to delete
        boolean isSuccess = sysRoleService.removeById(id);
        if(isSuccess){
            return Result.ok();
        } else{
            return Result.fail();
        }
    }


    //1.query all
    // http://localhost:8800/admin/system/sysRole/findAll
    @ApiOperation("查询所有记录")
    @GetMapping("/findAll")
    public Result findAllRole() {
        //TODO Simulation of abnormal effects ArithmeticException
        try {
            int i = 9/0;
        }catch(Exception e) {
            throw new CustomException(20001,"Custom exception handling is implemented");
        }

        //call service
        List<SysRole> list = sysRoleService.list();
        return Result.ok(list);
    }
}
