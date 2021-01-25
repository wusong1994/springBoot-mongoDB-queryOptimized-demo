package com.ws.app.core.web;

import com.ws.app.core.exception.BaseBusinessException;

import java.io.Serializable;

public class WebResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    private String code = SUCCESS_CODE;
    private String message = "";
    private Object data = null;
    public static String ERROR_CODE = "fail";
    public static String SUCCESS_CODE = "success";
    public WebResponse() {
    }

    public WebResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public WebResponse(Object data) {
        this.data = data;
    }

    public WebResponse(BaseBusinessException exception) {
        this.code = exception.getCode();
        this.message = exception.getMessage();
    }

    public WebResponse(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
