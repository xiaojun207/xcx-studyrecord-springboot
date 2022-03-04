package com.tencent.wxcloudrun.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

// mybatis-plus中的表名与类名完全一样
@Data
@TableName("WxAccount")
public class WxAccount implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id = 0;
    private Integer headUid;
    private String nickName;
    private String avatarUrl;
    private String mobile;
    private int gender; //性别 0：未知、1：男、2：女
    private int status;

    private String country;
    private String province;
    private String city;

    private String unionid;
    private String openid;
    private String sessionKey;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
