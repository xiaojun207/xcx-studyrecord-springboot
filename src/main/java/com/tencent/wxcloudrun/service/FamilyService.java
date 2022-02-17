package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.model.Family;
import com.tencent.wxcloudrun.model.WxAccount;

import java.util.List;

public interface FamilyService {
    List<WxAccount> findAll(Integer headUid);

    void add(Family family);
}
