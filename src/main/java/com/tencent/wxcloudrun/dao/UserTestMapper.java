package com.tencent.wxcloudrun.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tencent.wxcloudrun.model.UserTest;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserTestMapper extends BaseMapper<UserTest> {

  @Select({"<script>",
          "SELECT `id`, `uid`, `projectId`, `projectName`, `result`, `score`, `createdAt`, `updatedAt` FROM UserTest where uid IN",
          " <foreach collection='uidList' item='item' index='index' open='(' separator=',' close=')'>",
          " #{item}",
          " </foreach>",
          "<if test='projectId != null'>",
          " and projectId=#{projectId}",
          "</if>",
          " order by id desc limit 300",
          "</script>"})
  List<UserTest> findAllByUidList(@Param("uidList") List<Integer> uidList, @Param("projectId")Integer projectId);

  @Insert("INSERT INTO `UserTest`(`uid`, `projectId`, `projectName`, `result`, `score`)" +
          " VALUE(#{uid}, #{projectId}, #{projectName}, #{result}, #{score})")
  void insertUserTest(UserTest userTest);

  @Select("SELECT `id`, `uid`, `projectId`, `projectName`, `result`, `score`, `createdAt`, `updatedAt` FROM UserTest where uid=#{uid} order by id desc limit 1")
  UserTest findLastByUid(@Param("uid") Integer uid);


  @Select({"<script>",
          "SELECT `id`, `uid`, `projectId`, `projectName`, `result`, `score`, `createdAt`, `updatedAt` FROM UserTest where uid IN",
          " <foreach collection='uidList' item='item' index='index' open='(' separator=',' close=')'>",
          " #{item}",
          " </foreach>",
          " order by id desc limit 1",
          "</script>"})
  UserTest findLastByUidList(@Param("uidList") List<Integer> uidList);
}
