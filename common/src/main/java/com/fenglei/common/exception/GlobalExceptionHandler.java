package com.fenglei.common.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fenglei.common.result.Result;
import com.fenglei.common.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局系统异常处理
 *
 * @author qjui
 * @date 2020-02-25 13:54
 **/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public Result handleIllegalArgumentException(IllegalArgumentException e) throws IllegalArgumentException {
        log.error("异常，异常原因：{}", e.getMessage(), e);
        return Result.failed(e.getMessage());
    }


    @ExceptionHandler(JsonProcessingException.class)
    public Result handleJsonProcessingException(JsonProcessingException e) throws JsonProcessingException {
        log.error("异常，异常原因：{}", e.getMessage(), e);
        return Result.failed(e.getMessage());
    }

    @ExceptionHandler(BizException.class)
    public Result handleBizException(BizException e) throws BizException {
        if (e.getResultCode() != null) {
            return Result.failed(e.getResultCode());
        }
        return Result.failed(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) throws Exception {
        log.error("异常，异常原因：{}", e.getMessage(), e);
        if(e instanceof BizException){
            return Result.failed(e.getMessage());
        }else{
            return Result.exceptionError(e.getMessage());
        }
    }

    @ExceptionHandler(InvalidTokenException.class)
    public Result handleInvalidTokenException(InvalidTokenException e) throws InvalidTokenException {
        log.error("异常，异常原因：{}", e.getMessage(), e);
        return Result.failed("登录过期，请重新登录！");
    }

    /**
     * 用户不存在
     *
     * @param e
     * @return
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public Result handleUsernameNotFoundException(UsernameNotFoundException e) {
        log.error("异常，异常原因：{}", e.getMessage(), e);
        return Result.failed(ResultCode.USER_NOT_EXIST);
    }

    /**
     * 用户名和密码异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(InvalidGrantException.class)
    public Result handleInvalidGrantException(InvalidGrantException e) {
        log.error("异常，异常原因：{}", e.getMessage(), e);
        return Result.failed(ResultCode.USERNAME_OR_PASSWORD_ERROR);
    }


    /**
     * 账户异常(禁用、锁定、过期)
     *
     * @param e
     * @return
     */
    @ExceptionHandler({InternalAuthenticationServiceException.class})
    public Result handleInternalAuthenticationServiceException(InternalAuthenticationServiceException e) {
        log.error("异常，异常原因：{}", e.getMessage(), e);
        return Result.failed(e.getMessage());
    }
}
