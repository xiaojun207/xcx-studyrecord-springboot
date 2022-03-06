package com.tencent.wxcloudrun.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.tencent.wxcloudrun.dao.UserTestMapper;
import com.tencent.wxcloudrun.dao.WxAccountMapper;
import com.tencent.wxcloudrun.dto.UserTestRespDto;
import com.tencent.wxcloudrun.model.ApiException;
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
    FamilyService familyService;

    @Resource
    WxAccountMapper wxAccountMapper;

    @Resource
    PushMsgService pushMsgService;

    @Override
    public JSONObject myFamilyList(Integer uid) {
        List<Integer> uidList = familyService.getFamilyMemberUidList(uid);

        List<WxAccount> wxAccountList = wxAccountMapper.findAllByUidList(uidList);
        List<UserTest> list = userTestMapper.findAllByUidList(uidList);

        Map<Integer, String> wxAccountMap = wxAccountList.stream().collect(Collectors.toMap(WxAccount::getId, WxAccount::getNotNullNickName));
        List<UserTestRespDto> respDtoList = list.stream().map(a -> new UserTestRespDto(a)).collect(Collectors.toList());

        JSONObject res = new JSONObject();
        res.put("testList", respDtoList);
        res.put("nickNames", wxAccountMap);
        return res;
    }

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
        try {
            pushTestMsg(userTest);
        } catch (Exception e) {
            log.error("addUserTest.push.err:", e);
        }
    }

    @Override
    public void deleteUserTest(Integer optUid, Integer id) {
        UserTest userTest = userTestMapper.selectById(id);

        Integer headUid = familyService.getFamilyHeadUid(userTest.getUid(), false);
        if(headUid != optUid){
            throw new ApiException("你没有操作权限");
        }
        userTestMapper.deleteById(id);
    }

    @Override
    public UserTest findLastByUid(Integer uid) {
        List<Integer> uidList = familyService.getFamilyMemberUidList(uid);
        return userTestMapper.findLastByUid(uidList);
    }

    @Async
    public void pushTestMsg(UserTest userTest) {
        List<Integer> uidList = familyService.getFamilyMemberUidList(userTest.getUid());
        WxAccount testAccount = wxAccountMapper.findByWxUid(userTest.getUid());
        String title = "你的家庭成员" + testAccount.getNickName() + "增加了锻炼信息";
        String msg = userTest.getProjectName() + ":" + userTest.getResult() + "" + userTest.getUnit();

        List<WxAccount> wxAccountList = wxAccountMapper.findAllByUidList(uidList);
        for (WxAccount wxAccount : wxAccountList) {
            pushMsgService.pushMsgToUser(wxAccount, title, msg);
        }
    }

}
