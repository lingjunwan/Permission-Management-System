package com.project.system.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.model.system.SysLoginLog;
import com.project.model.system.SysUser;
import com.project.model.vo.SysUserQueryVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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
public interface LoginLogMapper extends BaseMapper<SysLoginLog> {

}
