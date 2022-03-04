package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.dao.FamilyMapper;
import com.tencent.wxcloudrun.dao.PreFamilyMapper;
import com.tencent.wxcloudrun.dao.WxAccountMapper;
import com.tencent.wxcloudrun.dto.MemberReqDto;
import com.tencent.wxcloudrun.model.ApiException;
import com.tencent.wxcloudrun.model.Family;
import com.tencent.wxcloudrun.model.PreFamily;
import com.tencent.wxcloudrun.model.WxAccount;
import com.tencent.wxcloudrun.service.FamilyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FamilyServiceImpl implements FamilyService {
    @Resource
    FamilyMapper familyMapper;
    @Resource
    WxAccountMapper wxAccountMapper;
    @Resource
    PreFamilyMapper preFamilyMapper;

    @Override
    public Integer getFamilyHeadUid(Integer uid) {
        Family family = familyMapper.findByUid(uid);
        if (family != null) {
            return family.getHeadUid();
        }
        return uid;
    }

    @Override
    public Family findByUid(Integer memberUid) {
        return familyMapper.findByUid(memberUid);
    }


    @Override
    public List<Integer> getFamilyMemberUidList(Integer memberUid) {
        Family family = this.findByUid(memberUid);
        List<Integer> uidList = Arrays.asList(memberUid);
        if (family != null) {
            List<Family> list = familyMapper.findAll(family.getHeadUid());
            uidList = list.stream().map(Family::getMemberUid).collect(Collectors.toList());
        }
        return uidList;
    }


    @Override
    public List<WxAccount> findAll(Integer memberUid) {
        Family family = this.findByUid(memberUid);
        List<Integer> uidList = Arrays.asList(memberUid);
        Integer headUid = memberUid;
        if (family != null) {
            headUid = family.getHeadUid();
            List<Family> list = familyMapper.findAll(family.getHeadUid());
            uidList = list.stream().map(Family::getMemberUid).collect(Collectors.toList());
        }

        List<WxAccount> wxAccountList = wxAccountMapper.findAllByUidList(uidList);
        for (WxAccount wxAccount : wxAccountList) {
            wxAccount.setHeadUid(headUid);
        }
        return wxAccountList;
    }

    @Override
    public List<WxAccount> findAllPreMemberList(Integer headUid) {
        List<PreFamily> list = preFamilyMapper.findByHeadUid(headUid);
        if (list.isEmpty()) {
            return Arrays.asList();
        }

        Map<Integer, PreFamily> preFamilyMap = list.stream().collect(Collectors.toMap(PreFamily::getMemberUid, o -> o));
        List<Integer> uidList = list.stream().map(PreFamily::getMemberUid).collect(Collectors.toList());

        List<WxAccount> wxAccountList = wxAccountMapper.findAllByUidList(uidList);
        for (WxAccount wxAccount : wxAccountList) {
            PreFamily preFamily = preFamilyMap.get(wxAccount.getId());
            wxAccount.setStatus(preFamily.getStatus());
            wxAccount.setHeadUid(headUid);
            wxAccount.setCreatedAt(preFamily.getCreatedAt());
        }
        return wxAccountList;
    }

    @Override
    public void joinFamily(Integer uid, String familyCode) {
        Family headFamily = familyMapper.findByUid(Integer.parseInt(familyCode));
        Integer headUid = headFamily.getHeadUid();

        Family oldFamily = familyMapper.findByUid(uid);
        if (oldFamily != null) {
            if (oldFamily.getHeadUid() == headUid) {
//                throw new ApiException("你已加入该家庭");
                return;
            }
            throw new ApiException("你已加入其它家庭");
        }

        PreFamily family = new PreFamily();
        family.setMemberUid(uid);
        family.setHeadUid(headUid);
        family.setStatus(0);
        preFamilyMapper.insert(family);
    }

    @Transactional
    @Override
    public void acceptMember(Integer optUid, Integer memberUid) {
        PreFamily preFamily = preFamilyMapper.findByUid(memberUid);
        if (preFamily.getHeadUid() == optUid) {
            if (preFamily.getStatus() != 0) {
                throw new ApiException("加入申请已处理");
            }

            Family family = new Family();
            family.setMemberUid(preFamily.getMemberUid());
            family.setHeadUid(preFamily.getHeadUid());
            family.setStatus(1);
            familyMapper.insertFamily(family);
            preFamily.setStatus(1);
            preFamilyMapper.updateById(preFamily);
        } else {
            throw new ApiException("你没有操作权限");
        }
    }

    @Override
    public void deleteMember(Integer optUid, Integer memberUid) {
        Family family = familyMapper.findByUid(memberUid);
        if (family.getHeadUid() == optUid) {
            familyMapper.deleteById(family.getId());
        } else {
            throw new ApiException("你没有操作权限");
        }
    }

    @Override
    public void addMember(Integer uid, MemberReqDto req) {
        Family headFamily = familyMapper.findByUid(uid);
        Integer headUid = headFamily.getHeadUid();

        WxAccount wxAccount = new WxAccount();
        wxAccount.setOpenid("in" + UUID.randomUUID().toString().replaceAll("-", ""));
        wxAccount.setNickName(req.getNickName());
        wxAccount.setGender(req.getGender() == null ? 0 : req.getGender());
        wxAccountMapper.insert(wxAccount);

        Family family = new Family();
        family.setMemberUid(wxAccount.getId());
        family.setHeadUid(headUid);
        family.setStatus(1);
        familyMapper.insertFamily(family);
    }
}
