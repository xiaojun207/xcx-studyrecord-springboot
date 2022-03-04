package com.tencent.wxcloudrun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tencent.wxcloudrun.dao.FamilyMapper;
import com.tencent.wxcloudrun.dao.UserTestMapper;
import com.tencent.wxcloudrun.dao.WxAccountMapper;
import com.tencent.wxcloudrun.model.Family;
import com.tencent.wxcloudrun.model.UserTest;
import com.tencent.wxcloudrun.model.WxAccount;
import com.tencent.wxcloudrun.service.FamilyService;
import com.tencent.wxcloudrun.service.PushMsgService;
import com.tencent.wxcloudrun.service.UserTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserTestServiceImpl implements UserTestService {
    @Resource
    UserTestMapper userTestMapper;
    @Resource
    FamilyMapper familyMapper;
    @Resource
    FamilyService familyService;

    @Resource
    WxAccountMapper wxAccountMapper;

    @Resource
    PushMsgService pushMsgService;

    @Override
    public List<UserTest> findAllByUid(Integer uid) {
        List<Integer> uidList = familyService.getFamilyMemberUidList(uid);

        List<WxAccount> wxAccountList = wxAccountMapper.findAllByUidList(uidList);
        Map<Integer, WxAccount> wxAccountMap = wxAccountList.stream().collect(Collectors.toMap(WxAccount::getId, o -> o));
        List<UserTest> res = userTestMapper.findAllByUidList(uidList);
        for (UserTest t : res) {
            WxAccount wxAccount = wxAccountMap.get(t.getUid());
            t.setNickName(wxAccount.getNickName());
        }

        return res;
    }

    @Override
    public void addUserTest(UserTest userTest) {
        userTest.setCreatedAt(LocalDateTime.now());
        userTest.setUpdatedAt(LocalDateTime.now());
        userTestMapper.insertUserTest(userTest);
        try{
            pushTestMsg(userTest);
        }catch (Exception e){
            log.error("addUserTest.push.err:", e);
        }
    }

    @Override
    public UserTest findLastByUid(Integer uid) {
//        Family family = familyMapper.findByUid(uid);
        return userTestMapper.findLastByUid(uid);
    }

    @Async
    public void pushTestMsg(UserTest userTest){
        Family family = familyMapper.findByUid(userTest.getUid());
        List<Family> list = familyMapper.findAll(family.getHeadUid());
        WxAccount testAccount = wxAccountMapper.findByWxUid(userTest.getUid());
        List<Integer> uidList = list.stream().map(Family::getMemberUid).collect(Collectors.toList());
        String title = "你的家庭成员" + testAccount.getNickName() + "增加了锻炼信息";
        String msg = userTest.getProjectName() + ":" + userTest.getResult() + "" + userTest.getUnit();
        for (Integer uid: uidList) {
            WxAccount wxAccount = wxAccountMapper.findByWxUid(uid);
            pushMsgService.pushMsgToUser(wxAccount, title, msg);
        }
    }

}
