package com.project.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mysql.cj.util.StringUtils;
import com.project.model.system.SysOperLog;
import com.project.model.vo.SysOperLogQueryVo;
import com.project.system.mapper.OperLogMapper;
import com.project.system.service.OperLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Lingjun
 * @date 2023/4/29 22:25
 */
@Service
public class OperLogServiceImpl implements OperLogService {

    @Autowired
    private OperLogMapper operLogMapper;

    @Override
    public void saveSysLog(SysOperLog sysOperLog) {
        operLogMapper.insert(sysOperLog);
    }

    ////pagination query
    @Override
    public IPage<SysOperLog> selectPage(Long page, Long limit, SysOperLogQueryVo sysOperLogQueryVo) {
        Page<SysOperLog> pageParam = new Page<>(page,limit);
        //get condition
        String title = sysOperLogQueryVo.getTitle();
        String operName = sysOperLogQueryVo.getOperName();
        String createTimeBegin = sysOperLogQueryVo.getCreateTimeBegin();
        String createTimeEnd = sysOperLogQueryVo.getCreateTimeEnd();
        QueryWrapper<SysOperLog> wrapper =new QueryWrapper<>();
        if (!StringUtils.isNullOrEmpty(title)){
            wrapper.eq("title",title);
        }
        if (!StringUtils.isNullOrEmpty(operName)){
            wrapper.eq("oper_name",operName);
        }
        if (!StringUtils.isNullOrEmpty(createTimeBegin)){
            wrapper.ge("create_time",createTimeBegin);
        }
        if (!StringUtils.isNullOrEmpty(createTimeEnd)){
            wrapper.ge("create_time",createTimeEnd);
        }
        //call mapper method
        IPage<SysOperLog> sysOperLogPage = operLogMapper.selectPage(pageParam, wrapper);
        return sysOperLogPage;
    }
}
