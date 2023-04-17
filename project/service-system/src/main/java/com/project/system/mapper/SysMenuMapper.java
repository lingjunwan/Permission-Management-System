package com.project.system.mapper;

import com.project.model.system.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author lingjun
 * @since 2023-04-11
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    //find menu permission data by userid
    List<SysMenu> findMenuListUserId(@Param("userId") String userId);
}
