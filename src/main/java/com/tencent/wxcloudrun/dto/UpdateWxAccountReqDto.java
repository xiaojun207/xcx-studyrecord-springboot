package com.tencent.wxcloudrun.dto;

import com.tencent.wxcloudrun.model.WxAccount;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UpdateWxAccountReqDto extends WxAccount implements Serializable {

}
