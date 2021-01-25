package com.ws.app.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ws.app.core.po.SysUserPo;
import com.ws.app.core.util.PageUtils;
import com.ws.app.dto.PageResultDTO;
import com.ws.app.mapper.SysUserMapper;
import com.ws.app.dto.SysUserDTO;
import com.ws.app.request.PageRequest;
import com.ws.app.service.SysUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    SysUserMapper sysUserMapper;

    @Override
    public int addUser(SysUserPo userPo) {
        return sysUserMapper.insert(userPo);
    }

    @Override
    public SysUserPo findByUserId(Long userId) {
        return sysUserMapper.findByUserId(userId);
    }

    @Override
    public SysUserDTO selectByUserId(Long userId) {
        return sysUserMapper.selectByUserId(userId);
    }

    @Override
    public PageResultDTO findPage(PageRequest pageRequest) {
        PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());
        List<SysUserPo> sysUserPos = sysUserMapper.selectAll();
        return PageUtils.getPageResult(new PageInfo<>(sysUserPos));
    }
}
