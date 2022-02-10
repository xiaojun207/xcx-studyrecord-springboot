package com.tencent.wxcloudrun.dto;

import lombok.Data;

//import javax.validation.constraints.NotEmpty;

@Data
public class CodeReqDto {
//    @NotEmpty(message = "缺少参数code或code不合法")
    private String code;
}
