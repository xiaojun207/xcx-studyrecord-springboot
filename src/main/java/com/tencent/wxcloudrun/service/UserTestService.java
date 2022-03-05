package com.tencent.wxcloudrun.service;

import com.alibaba.fastjson.JSONObject;
import com.tencent.wxcloudrun.model.UserTest;

import java.util.List;

public interface UserTestService {

    JSONObject myFamilyList(Integer uid);
    List<UserTest> findAllByUid(Integer uid);
    void addUserTest(UserTest userTest);

    UserTest findLastByUid(Integer uid);

}
