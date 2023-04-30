package com.project.system.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.model.system.SysLoginLog;
import com.project.model.system.SysOperLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author lingjun
 * @since 2023-04-09
 */
@Mapper
@Repository
public interface OperLogMapper extends BaseMapper<SysOperLog> {

}
