package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.model.WxAccount;
import com.tencent.wxcloudrun.service.FamilyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
