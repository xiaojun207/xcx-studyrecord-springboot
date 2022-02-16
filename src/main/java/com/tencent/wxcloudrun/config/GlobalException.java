package com.tencent.wxcloudrun.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalException {
    String MSG_UserSessionNotExist = "Missing session attribute 'wxAccount' of type WxAccount";
    String MSG_TokenInvalid = "Token Invalid";

    /**
     * 全局异常处理
     */
    @ExceptionHandler
    public ApiResponse handleException(Exception e) {
        String msg = e.getMessage();
        String className = e.getClass().getName();
        log.error("handleException.class:" + className + "，msg:" + e.getMessage());

        if(MSG_TokenInvalid.equals(msg) || MSG_UserSessionNotExist.equals(msg)){
            return ApiResponse.error(-101,"未登录");
        }else {

        }
        return ApiResponse.error(e.getMessage());
    }
}
