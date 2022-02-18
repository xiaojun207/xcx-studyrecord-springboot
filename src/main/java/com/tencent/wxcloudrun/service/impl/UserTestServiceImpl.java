package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.dao.FamilyMapper;
import com.tencent.wxcloudrun.dao.UserTestMapper;
import com.tencent.wxcloudrun.dao.WxAccountMapper;
import com.tencent.wxcloudrun.model.Family;
import com.tencent.wxcloudrun.model.UserTest;
import com.tencent.wxcloudrun.model.WxAccount;
import com.tencent.wxcloudrun.service.PushMsgService;
import com.tencent.wxcloudrun.service.UserTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
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

    @Resource
    PushMsgService pushMsgService;

    @Override
    public List<UserTest> findAllByUid(Integer uid) {
        Family family = familyMapper.findByUid(uid);
        List<Family> list = familyMapper.findAll(family.getHeadUid());
        List<Integer> uidList = list.stream().map(Family::getMemberUid).collect(Collectors.toList());
        List<WxAccount> wxAccountList = wxAccountMapper.findAllByUidList(uidList);
        Map<Integer, String> wxAccountMap = wxAccountList.stream().collect(Collectors.toMap(WxAccount::getId, WxAccount::getNickName));
        List<UserTest> res = userTestMapper.findAllByUidList(uidList);
        for (UserTest t : res) {
            String nickname = wxAccountMap.get(t.getUid());
            t.setNickName(nickname);
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
