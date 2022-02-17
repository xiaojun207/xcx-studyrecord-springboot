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
          " order by id desc",
          "</script>"})
  List<UserTest> findAllByUidList(@Param("uidList") List<Integer> uidList);

  @Insert("INSERT INTO `UserTest`(`uid`, `projectId`, `projectName`, `result`, `score`)" +
          " VALUE(#{uid}, #{projectId}, #{projectName}, #{result}, #{score})")
  void insertUserTest(UserTest userTest);

}
