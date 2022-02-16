package com.tencent.wxcloudrun.model;

public class ApiException extends RuntimeException{
    public ApiException(String msg) {
        super(msg);
    }
}
