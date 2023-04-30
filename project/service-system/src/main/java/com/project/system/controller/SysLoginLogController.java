package com.project.system.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.project.common.result.Result;
import com.project.model.system.SysLoginLog;
import com.project.model.vo.SysLoginLogQueryVo;
import com.project.system.service.LoginLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Lingjun
 * @date 2023/4/30 10:57
 */
@Api(value = "SysLoginLog Management", tags = "SysLoginLog Management")
@RestController
@RequestMapping(value="/admin/system/sysLoginLog")
@SuppressWarnings({"unchecked", "rawtypes"})
public class SysLoginLogController {
    @Autowired
    private LoginLogService loginLogService;


    //pagination login log
    @ApiOperation(value = "Pagination List")
    @GetMapping("{page}/{limit}")
    public Result index(
            @ApiParam(name = "page", value = "present page", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "page limit", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "sysLoginLogVo", value = "searchObj", required = false)
            SysLoginLogQueryVo sysLoginLogQueryVo) {

        IPage<SysLoginLog> pageModel = loginLogService.selectPage(page,limit, sysLoginLogQueryVo);
        return Result.ok(pageModel);
    }
}
