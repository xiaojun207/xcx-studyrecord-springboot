package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.WxAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface WxAccountMapper {

  WxAccount findByWxOpenid(@Param("openid") String openid);

  void upsertWxAccount(WxAccount wxAccount);

}
