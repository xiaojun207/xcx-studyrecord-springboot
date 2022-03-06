package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.dto.MemberReqDto;
import com.tencent.wxcloudrun.model.Family;
import com.tencent.wxcloudrun.model.WxAccount;

import java.util.List;

public interface FamilyService {

    Integer getFamilyHeadUid(Integer uid, boolean autoCreate);

    Family findByUid(Integer memberUid);

    List<Integer> getFamilyMemberUidList(Integer memberUid);

    List<WxAccount> findAll(Integer headUid);

    List<WxAccount> findAllPreMemberList(Integer headUid);

    Integer joinFamily(Integer uid, String familyCode);

    void acceptMember(Integer optUid, Integer memberUid);

    void deleteMember(Integer optUid, Integer memberUid);

    void addMember(Integer uid, MemberReqDto req);
}
