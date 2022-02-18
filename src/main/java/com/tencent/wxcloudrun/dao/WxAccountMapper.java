package com.tencent.wxcloudrun.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tencent.wxcloudrun.model.UserTest;
import com.tencent.wxcloudrun.model.WxAccount;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface WxAccountMapper extends BaseMapper<WxAccount> {

  @Select("SELECT `id`, `nickName`, `avatarUrl`, `mobile`, `gender`, `country`, `province`, `city`, `openid`, `unionid`, `sessionKey`, `createdAt`, `updatedAt`" +
          " FROM WxAccount WHERE id = #{id}")
  WxAccount findByWxUid(@Param("id") Integer id);


  @Select({"<script>",
          "SELECT `id`, `nickName`, `avatarUrl`, `mobile`, `gender`, `country`, `province`, `city`, `openid`, `unionid`, `sessionKey`, `createdAt`, `updatedAt`",
          " FROM WxAccount WHERE id in",
          " <foreach collection='uidList' item='item' index='index' open='(' separator=',' close=')'>",
          " #{item}",
          " </foreach>",
          " order by id desc",
          "</script>"})
  List<WxAccount> findAllByUidList(@Param("uidList") List<Integer> uidList);

  @Select("SELECT `id`, `openid`, `unionid`, `sessionKey`, `createdAt`, `updatedAt` FROM WxAccount WHERE openid = #{openid}")
  WxAccount findByWxOpenid(@Param("openid") String openid);

  @Insert(" INSERT INTO `WxAccount`(`id`, `openid`, `unionid`, `sessionKey`)" +
          " VALUE(#{id}, #{openid}, #{unionid}, #{sessionKey})" +
          " ON DUPLICATE KEY UPDATE openid=#{openid}, unionid=#{unionid}, sessionKey=#{sessionKey}")
  void upsertWxAccount(WxAccount wxAccount);

}
