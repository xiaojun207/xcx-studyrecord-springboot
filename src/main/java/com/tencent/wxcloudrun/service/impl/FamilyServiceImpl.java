package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.dao.FamilyMapper;
import com.tencent.wxcloudrun.dao.WxAccountMapper;
import com.tencent.wxcloudrun.model.Family;
import com.tencent.wxcloudrun.model.WxAccount;
import com.tencent.wxcloudrun.service.FamilyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FamilyServiceImpl implements FamilyService {
    @Resource
    FamilyMapper familyMapper;
    @Resource
    WxAccountMapper wxAccountMapper;

    @Override
    public List<WxAccount> findAll(Integer uid) {
        Family family = familyMapper.findByUid(uid);
        List<Family> list = familyMapper.findAll(family.getHeadUid());
        List<WxAccount> res = new ArrayList<>();
        for (Family f : list) {
            WxAccount wxAccount = wxAccountMapper.findByWxUid(f.getMemberUid());
            wxAccount.setHeadUid( family.getHeadUid());
            res.add(wxAccount);
        }
        return res;
    }

    @Override
    public void add(Family family1){
        Family family = new Family();
        familyMapper.insertFamily(family);
    }
}
