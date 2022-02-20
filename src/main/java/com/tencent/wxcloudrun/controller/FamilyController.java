package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.FamilyMemberDto;
import com.tencent.wxcloudrun.dto.JoinFamilyDto;
import com.tencent.wxcloudrun.model.WxAccount;
import com.tencent.wxcloudrun.service.FamilyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * counter控制器
 */
@Slf4j
@RequestMapping("/family")
@RestController
public class FamilyController {
  @Resource
  FamilyService familyService;

  @GetMapping(value = "/memberList")
  ApiResponse memberList(WxAccount wxAccount) {
    return ApiResponse.ok(familyService.findAll(wxAccount.getId()));
  }

  @GetMapping(value = "/preMemberList")
  ApiResponse preMemberList(WxAccount wxAccount) {
    return ApiResponse.ok(familyService.findAllPreMemberList(wxAccount.getId()));
  }

  @PostMapping(value = "/joinFamily")
  ApiResponse joinFamily(WxAccount wxAccount, @RequestBody JoinFamilyDto req) {
    familyService.joinFamily(wxAccount.getId(), req.getFamilyCode());
    return ApiResponse.ok();
  }

  @PostMapping(value = "/acceptMember")
  ApiResponse acceptMember(WxAccount wxAccount, @RequestBody FamilyMemberDto req) {
    familyService.acceptMember(wxAccount.getId(), req.getMemberUid());
    return ApiResponse.ok();
  }

  @PostMapping(value = "/deleteMember")
  ApiResponse deleteMember(WxAccount wxAccount, @RequestBody FamilyMemberDto req) {
    familyService.deleteMember(wxAccount.getId(), req.getMemberUid());
    return ApiResponse.ok();
  }

}
