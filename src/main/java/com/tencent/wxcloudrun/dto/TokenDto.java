package com.tencent.wxcloudrun.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenDto {
    private String token;
    private Integer uid;

    private Integer headUid;

    private String nickName;
    private String avatarUrl;
    private int gender; //性别 0：未知、1：男、2：女

}
