package com.ws.app.core.exception;

import java.util.Locale;

public class BaseBusinessException extends RuntimeException {

    private static final long serialVersionUID = 7737887002287358220L;
    private String code;
    private IBaseBusinessError message;

    public BaseBusinessException(String message) {
        super(message);
        this.message = null;
        this.code = "fail";
    }

    public BaseBusinessException(String code, String message) {
        super(message);
        this.message = null;
        this.code = code;
    }

    public BaseBusinessException(IBaseBusinessError error, String message) {
        super(message);
        this.message = null;
        this.code = error.getCode();
    }

    public BaseBusinessException(IBaseBusinessError message) {
        this(message.getCode(), message.getMessage());
        this.message = message;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage(Locale locale) {
        return this.message.getMessage(locale);
    }
}
