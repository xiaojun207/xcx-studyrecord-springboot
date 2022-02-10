package com.tencent.wxcloudrun.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class WxAccount implements Serializable {

    private Integer id;
    private String unionid;
    private String openid;
    private String sessionKey;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
