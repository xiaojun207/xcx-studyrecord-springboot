package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.dao.UserTestMapper;
import com.tencent.wxcloudrun.model.UserTest;
import com.tencent.wxcloudrun.service.UserTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class UserTestServiceImpl implements UserTestService {
    @Resource
    UserTestMapper userTestMapper;

    @Override
    public List<UserTest> findAllByUid(Integer uid) {
        return userTestMapper.findAllByUid(uid);
    }

    @Override
    public void addUserTest(UserTest userTest) {
        userTestMapper.insertUserTest(userTest);
    }
}
