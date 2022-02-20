package com.tencent.wxcloudrun.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("WxAccount")
public class WxAccount implements Serializable {

    private Integer id = 0;
    private Integer headUid;
    private String nickName;
    private String avatarUrl;
    private String mobile;
    private int gender; //性别 0：未知、1：男、2：女

    private String country;
    private String province;
    private String city;

    private String unionid;
    private String openid;
    private String sessionKey;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
