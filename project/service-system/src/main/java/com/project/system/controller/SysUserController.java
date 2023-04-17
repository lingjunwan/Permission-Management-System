package com.project.system.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.common.result.Result;
import com.project.common.utils.MD5;
import com.project.model.system.SysUser;
import com.project.model.vo.SysUserQueryVo;
import com.project.system.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author lingjun
 * @since 2023-04-09
 */

@Api(tags = "用户管理接口")
@RestController
@RequestMapping("/admin/system/sysUser")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @ApiOperation(value = "更改用户状态")
    @GetMapping("updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable String id,
                               @PathVariable Integer status) {
        sysUserService.updateStatus(id, status);
        return Result.ok();
    }

    @ApiOperation(value = "用户列表")
    @GetMapping("/{page}/{limit}")
    public Result list(
            //@ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            //@ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,

            //@ApiParam(name = "userQueryVo", value = "查询对象", required = false)
            SysUserQueryVo sysUserQueryVo) {
        //Create page object
        Page<SysUser> pageParam = new Page<>(page, limit);
        //Call service method
        IPage<SysUser> pageModel = sysUserService.selectPage(pageParam, sysUserQueryVo);
        return Result.ok(pageModel);
    }
    @ApiOperation(value = "添加用户")
    @PostMapping("save")
    public Result save(@RequestBody SysUser user) {
        //Encrypt the password MD5
        String encrypt = MD5.encrypt(user.getPassword());
        user.setPassword(encrypt);
       boolean is_Success = sysUserService.save(user);
       if(is_Success){
           return Result.ok();
       }else {
           return Result.fail();
        }
    }
    @ApiOperation(value = "根据id查询")
    @GetMapping("/getUser/{id}")
    public Result getUser(@PathVariable String id) {
        SysUser user = sysUserService.getById(id);
        return Result.ok(user);
    }

    @ApiOperation(value = "修改用户")
    @PostMapping("update")
    public Result update(@RequestBody SysUser user) {
        boolean is_Success = sysUserService.updateById(user);
        if(is_Success){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }
    @ApiOperation(value = "删除用户")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable String id) {
        boolean is_Success = sysUserService.removeById(id);
        return Result.ok();
    }
}

