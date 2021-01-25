package com.ws.app;

import com.ws.app.core.po.SbDevicePo;
import com.ws.app.core.po.SysUserPo;
import com.ws.app.dto.SysUserDTO;
import com.ws.app.mapper.SbDeviceMapper;
import com.ws.app.service.SysUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestService {

    @Autowired
    SysUserService sysUserService;

    @Resource
    SbDeviceMapper sbDeviceMapper;

    @Test
    public void testInsert(){
        SysUserPo sysUserPo = new SysUserPo();
        sysUserPo.setUserName("ws");
        sysUserService.addUser(sysUserPo);
    }

    @Test
    public void testQuery(){
        SysUserPo sysUserPo = sysUserService.findByUserId(1l);
        System.out.println(sysUserPo);

        SysUserDTO sysUserDTO = sysUserService.selectByUserId(4l);
        System.out.println(sysUserDTO);
    }

    @Test
    public void testSbDeviceQuery(){
        SbDevicePo po = sbDeviceMapper.selectByPrimaryKey(1L);
        System.out.println(po);
    }
}
