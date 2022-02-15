package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.WxAccount;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface WxAccountMapper {

  @Select("SELECT `id`, `nickName`, `avatarUrl`, `mobile`, `gender`, `country`, `province`, `city`, `openid`, `unionid`, `sessionKey`, `createdAt`, `updatedAt`" +
          " FROM WxAccount WHERE id = #{id}")
  WxAccount findByWxUid(@Param("id") Integer id);

  @Select("SELECT `id`, `openid`, `unionid`, `sessionKey`, `createdAt`, `updatedAt` FROM WxAccount WHERE openid = #{openid}")
  WxAccount findByWxOpenid(@Param("openid") String openid);

  @Insert(" INSERT INTO `WxAccount`(`id`, `openid`, `unionid`, `sessionKey`)" +
          " VALUE(#{id}, #{openid}, #{unionid}, #{sessionKey})" +
          " ON DUPLICATE KEY UPDATE openid=#{openid}, unionid=#{unionid}, sessionKey=#{sessionKey}")
  void upsertWxAccount(WxAccount wxAccount);

}
