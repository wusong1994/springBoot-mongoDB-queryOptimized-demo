package com.ws.app.service;

import com.ws.app.core.po.SysUserPo;
import com.ws.app.dto.PageResultDTO;
import com.ws.app.dto.SysUserDTO;
import com.ws.app.request.PageRequest;

public interface SysUserService {

    int addUser(SysUserPo userPo);

    SysUserPo findByUserId(Long userId);

    SysUserDTO selectByUserId(Long userId);

    PageResultDTO findPage(PageRequest pageRequest);
}
