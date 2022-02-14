package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.dao.FamilyMapper;
import com.tencent.wxcloudrun.dao.UserTestMapper;
import com.tencent.wxcloudrun.dao.WxAccountMapper;
import com.tencent.wxcloudrun.model.Family;
import com.tencent.wxcloudrun.model.UserTest;
import com.tencent.wxcloudrun.model.WxAccount;
import com.tencent.wxcloudrun.service.UserTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserTestServiceImpl implements UserTestService {
    @Resource
    UserTestMapper userTestMapper;
    @Resource
    FamilyMapper familyMapper;
    @Resource
    WxAccountMapper wxAccountMapper;

    @Override
    public List<UserTest> findAllByUid(Integer uid) {
        Family family = familyMapper.findByUid(uid);
        List<Family> list = familyMapper.findAll(family.getHeadUid());
        List<Integer> uidList = list.stream().map(Family::getMemberUid).collect(Collectors.toList());
        List<UserTest> res = userTestMapper.findAllByUidList(uidList);
        for (UserTest t : res) {
            WxAccount wxAccount = wxAccountMapper.findByWxUid(t.getUid());
            t.setNickName(wxAccount.getNickName());
        }

        return res;
    }

    @Override
    public void addUserTest(UserTest userTest) {
        userTestMapper.insertUserTest(userTest);
    }
}
