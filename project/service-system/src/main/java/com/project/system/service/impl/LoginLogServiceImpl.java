package com.project.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mysql.cj.util.StringUtils;
import com.project.model.system.SysLoginLog;
import com.project.model.vo.SysLoginLogQueryVo;
import com.project.system.mapper.LoginLogMapper;
import com.project.system.service.LoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Lingjun
 * @date 2023/4/29 20:47
 */
@Service
public class LoginLogServiceImpl implements LoginLogService {
    @Autowired
    private LoginLogMapper loginLogMapper;
    @Override
    public void recordLoginLog(String username, Integer status, String ipaddr, String message) {
        SysLoginLog sysLoginLog = new SysLoginLog();
        sysLoginLog.setUsername(username);
        sysLoginLog.setStatus(status);
        sysLoginLog.setIpaddr(ipaddr);
        sysLoginLog.setMsg(message);
        loginLogMapper.insert(sysLoginLog);
    }

    @Override
    public IPage<SysLoginLog> selectPage(Long page, Long limit, SysLoginLogQueryVo sysLoginLogQueryVo) {
        //create page obj
        Page<SysLoginLog> pageParam = new Page<>(page,limit);
        //get condition value
        String username = sysLoginLogQueryVo.getUsername();
        String createTimeBegin = sysLoginLogQueryVo.getCreateTimeBegin();
        String createTimeEnd = sysLoginLogQueryVo.getCreateTimeEnd();
        QueryWrapper<SysLoginLog> wrapper = new QueryWrapper<>();
        if (!StringUtils.isNullOrEmpty(username)){
            wrapper.eq("username",username);
        }
        if (!StringUtils.isNullOrEmpty(createTimeBegin)){
            wrapper.ge("create_time",createTimeBegin);
        }
        if (!StringUtils.isNullOrEmpty(createTimeEnd)){
            wrapper.le("create_time",createTimeEnd);
        }
        //call mapper method
        IPage<SysLoginLog> pageModel = loginLogMapper.selectPage(pageParam, wrapper);
        return pageModel;
    }
}
