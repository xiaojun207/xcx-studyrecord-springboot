package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.UserTest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserTestMapper {

  List<UserTest> findAllByUid(@Param("uid") Integer uid);

  void insertUserTest(UserTest userTest);

}
