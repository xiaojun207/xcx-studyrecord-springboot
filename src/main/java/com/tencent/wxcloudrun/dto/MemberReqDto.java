package com.tencent.wxcloudrun.dto;

import lombok.Data;

@Data
public class MemberReqDto {
    String nickName;
    Integer uid;
    Integer gender;
    Integer grade;
}
