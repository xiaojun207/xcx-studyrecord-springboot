package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.model.WxAccount;

import java.util.List;

public interface FamilyService {

    Integer getFamilyHeadUid(Integer uid);

    List<Integer> getFamilyMemberUidList(Integer uid);

    List<WxAccount> findAll(Integer headUid);

    List<WxAccount> findAllPreMemberList(Integer headUid);

    void joinFamily(Integer uid, String familyCode);

    void acceptMember(Integer optUid, Integer memberUid);

    void deleteMember(Integer optUid, Integer memberUid);
}
