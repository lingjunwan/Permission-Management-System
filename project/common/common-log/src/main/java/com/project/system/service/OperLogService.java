package com.project.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.project.model.system.SysOperLog;
import com.project.model.vo.SysOperLogQueryVo;

/**
 * @author Lingjun
 * @date 2023/4/29 22:17
 */
public interface OperLogService {
    public void saveSysLog(SysOperLog sysOperLog);
    //pagination query
    IPage<SysOperLog> selectPage(Long page, Long limit, SysOperLogQueryVo sysOperLogQueryVo);
}
