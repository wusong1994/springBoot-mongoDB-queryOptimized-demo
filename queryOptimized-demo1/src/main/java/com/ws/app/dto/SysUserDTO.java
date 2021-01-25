package com.ws.app.dto;

import java.io.Serializable;

public class SysUserDTO implements Serializable {

    private static final long serialVersionUID = 215669299641730431L;

    private Long userId;

    private String userName;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
