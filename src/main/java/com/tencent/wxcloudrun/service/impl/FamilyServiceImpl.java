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
import java.util.concurrent.ConcurrentHashMap;
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

    static ConcurrentHashMap<Integer, Integer> uidMapHeadUid = new ConcurrentHashMap<>();

    /**
     * 返回headUid，如果不存在family，则自动创建
     * @param uid
     * @return
     */
    @Override
    public Integer getFamilyHeadUid(Integer uid, boolean autoCreate) {
        if(uidMapHeadUid.containsKey(uid)){
            return uidMapHeadUid.get(uid);
        }

        Family family = familyMapper.findByUid(uid);
        if (family != null) {
            uidMapHeadUid.put(family.getMemberUid(), family.getHeadUid());
            return family.getHeadUid();
        }
        if(autoCreate){
            addMember(uid, uid);
            uidMapHeadUid.put(uid, uid);
        }
        return uid;
    }

    private void addMember(Integer memberUid, Integer headUid){
        Family family = new Family();
        family.setMemberUid(memberUid);
        family.setHeadUid(headUid);
        family.setStatus(1);
        familyMapper.insertFamily(family);
        uidMapHeadUid.put(family.getMemberUid(), family.getHeadUid());
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
            uidList = familyMapper.findMemberUidList(family.getHeadUid());
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
            uidList = familyMapper.findMemberUidList(family.getHeadUid());
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

        Map<Integer, PreFamily> preFamilyMap = list.stream().collect(Collectors.toMap(PreFamily::getMemberUid, o -> o, (V1, V2) -> V1));
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
        Integer headUid = getFamilyHeadUid(Integer.parseInt(familyCode), true);

        Family oldFamily = familyMapper.findByUid(uid);
        if (oldFamily != null) {
            if (oldFamily.getHeadUid() == headUid) {
                // 重复调用，默认成功，不返回错误
                // throw new ApiException("你已加入该家庭");
                return;
            }else if(oldFamily.getHeadUid() == uid){
               // 自己一个人的
                Integer count = familyMapper.findCount(uid);
                if (count == 1){
                    deleteMember(uid, uid);
                }else {
                    throw new ApiException("请先移除你的家庭");
                }
            }else {
                throw new ApiException("你已加入其它家庭");
            }
        }

        PreFamily oldPreFamily = preFamilyMapper.findByUid(uid);
        if (oldPreFamily != null && oldPreFamily.getStatus() == 0) {
            throw new ApiException("你已申请加入,请通知【家庭管理员】同意");
        }

        PreFamily family = new PreFamily();
        family.setMemberUid(uid);
        family.setHeadUid(headUid);
        family.setStatus(0);
        preFamilyMapper.insert(family);

        if(oldPreFamily == null ) {
            // 首次被邀请，自动同意加入
            acceptMember(headUid, uid);
        }
    }

    @Transactional
    @Override
    public void acceptMember(Integer optUid, Integer memberUid) {
        PreFamily preFamily = preFamilyMapper.findByUid(memberUid);
        if (preFamily.getHeadUid() == optUid) {
            if (preFamily.getStatus() != 0) {
//                throw new ApiException("加入申请已处理");
                return;
            }

            addMember(preFamily.getMemberUid(), preFamily.getHeadUid());

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
            uidMapHeadUid.remove(family.getMemberUid(), family.getHeadUid());
        } else {
            throw new ApiException("你没有操作权限");
        }
    }

    @Transactional
    @Override
    public void addMember(Integer uid, MemberReqDto req) {
        Integer headUid = getFamilyHeadUid(uid, true);

        WxAccount wxAccount = new WxAccount();
        wxAccount.setOpenid("in-" + uid + "-" + UUID.randomUUID().toString().replaceAll("-", ""));
        wxAccount.setNickName(req.getNickName());
        wxAccount.setGender(req.getGender() == null ? 0 : req.getGender());
        wxAccountMapper.insert(wxAccount);

        addMember(wxAccount.getId(), headUid);
    }
}
