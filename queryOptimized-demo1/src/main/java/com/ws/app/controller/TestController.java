package com.ws.app.controller;

import com.ws.app.core.exception.BaseBusinessException;
import com.ws.app.core.web.WebResponse;
import com.ws.app.dto.SysUserDTO;
import com.ws.app.request.PageRequest;
import com.ws.app.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    SysUserService sysUserService;

    @RequestMapping("success")
    public WebResponse testSuccess() {
        SysUserDTO sysUserDTO = sysUserService.selectByUserId(4l);
        System.out.println(sysUserDTO);
        return new WebResponse(sysUserDTO);
    }

    @RequestMapping("fail")
    public WebResponse testFail() {
        throw new BaseBusinessException("业务异常");
    }


    /**
     * 分页
     * @param pageRequest
     * @return
     */
    @RequestMapping("page")
    public WebResponse getPage(PageRequest pageRequest) {
        return new WebResponse(sysUserService.findPage(pageRequest));
    }

}
