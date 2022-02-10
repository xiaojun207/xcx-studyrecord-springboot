package com.tencent.wxcloudrun.config;

import lombok.Data;

import java.util.HashMap;

@Data
public final class ApiResponse {

  private Integer code;
  private String msg;
  private Object data;

  private ApiResponse(int code, String msg, Object data) {
    this.code = code;
    this.msg = msg;
    this.data = data;
  }

  public static ApiResponse ok() {
    return new ApiResponse(0, "成功", null);
  }

  public static ApiResponse ok(Object data) {
    return new ApiResponse(200, "成功", data);
  }

  public static ApiResponse error(int code, String msg) {
    return new ApiResponse(code, msg, null);
  }

  public static ApiResponse error(String msg) {
    return new ApiResponse(-1, msg, null);
  }

}
