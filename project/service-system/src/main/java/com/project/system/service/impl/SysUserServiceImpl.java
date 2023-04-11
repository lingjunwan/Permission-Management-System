package com.project.system.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.model.system.SysUser;
import com.project.model.vo.SysUserQueryVo;
import com.project.system.mapper.SysUserMapper;
import com.project.system.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author lingjun
 * @since 2023-04-09
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    //User List
    @Override
    public IPage<SysUser> selectPage(Page<SysUser> pageParam, SysUserQueryVo sysUserQueryVo) {
        return baseMapper.selectPage(pageParam,sysUserQueryVo);
    }

    //Update User Status
    @Override
    public void updateStatus(String id, Integer status) {
        //Search by user id
        SysUser sysUser = baseMapper.selectById(id);
        //Set update status
        sysUser.setStatus(status);
        //Call method to update
        baseMapper.updateById(sysUser);
    }
}
