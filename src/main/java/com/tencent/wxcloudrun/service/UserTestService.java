package com.tencent.wxcloudrun.service;

import com.alibaba.fastjson.JSONObject;
import com.tencent.wxcloudrun.model.UserTest;

import java.util.List;

public interface UserTestService {

    JSONObject myFamilyList(Integer uid, Integer projectId);
    List<UserTest> findAllByUid(Integer uid);
    void addUserTest(UserTest userTest);

    void deleteUserTest(Integer optUid, Integer id);

    UserTest findLastByUid(Integer uid);

}
