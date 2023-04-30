package com.project.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.project.model.system.SysLoginLog;
import com.project.model.vo.SysLoginLogQueryVo;

/**
 * @author Lingjun
 * @date 2023/4/29 20:43
 */

public interface LoginLogService {
    //Login Log
    public void recordLoginLog(String username, Integer status, String ipaddr, String message);


    IPage<SysLoginLog> selectPage(Long page, Long limit, SysLoginLogQueryVo sysLoginLogQueryVo);

}
