package com.ws.app.core.web;

import com.ws.app.core.exception.BaseBusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletResponse;

/**
 * 自定义异常统一处理
 */
@RestControllerAdvice
public class CustomErrorController {
    private static final Logger logger = LoggerFactory.getLogger(CustomErrorController.class);

    /**
     * 应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器
     *
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
    }


    /**
     * 业务异常处理
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = BaseBusinessException.class)
    public WebResponse ExceptionHandler(HttpServletResponse response, BaseBusinessException ex) {
        return new WebResponse(WebResponse.ERROR_CODE, ex.getMessage());
    }

    /**
     * 400 - Bad Request
     * 参数绑定校验异常捕捉处理
     */

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public WebResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        String msg = "";
        for (FieldError error : bindingResult.getFieldErrors()) {
            msg += error.getDefaultMessage();
            logger.error(msg);
            break;
        }
        return new WebResponse(WebResponse.ERROR_CODE, ex.getMessage());
    }

    /**
     * 400 - Bad Request
     * 参数绑定校验异常捕捉处理
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public WebResponse handleBindException(BindException ex) {
        FieldError fieldError = ex.getFieldError();
        return new WebResponse(WebResponse.ERROR_CODE, ex.getMessage());
    }

    /**
     * 404 - Not Found
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public WebResponse noHandlerFoundException(NoHandlerFoundException ex) {
        return new WebResponse(WebResponse.ERROR_CODE, ex.getMessage());
    }

    /**
     * 405 - Method Not Allowed
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public WebResponse handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return new WebResponse(WebResponse.ERROR_CODE, ex.getMessage());
    }

    /**
     * 全局异常捕捉处理
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public WebResponse ExceptionHandler(HttpServletResponse response, Exception ex) {
        return new WebResponse(WebResponse.ERROR_CODE, ex.getMessage());
    }
}
