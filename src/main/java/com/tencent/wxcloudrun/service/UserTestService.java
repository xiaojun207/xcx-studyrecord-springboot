package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.model.UserTest;

import java.util.List;

public interface UserTestService {

    List<UserTest> findAllByUid(Integer uid);
    void addUserTest(UserTest userTest);

}
