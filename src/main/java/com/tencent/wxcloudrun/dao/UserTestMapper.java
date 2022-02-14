package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.UserTest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserTestMapper {

  List<UserTest> findAllByUidList(@Param("uidList") List<Integer> uidList);

  void insertUserTest(UserTest userTest);

}
