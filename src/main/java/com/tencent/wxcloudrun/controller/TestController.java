package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.model.UserTest;
import com.tencent.wxcloudrun.model.WxAccount;
import com.tencent.wxcloudrun.service.UserTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * counter控制器
 */
@Slf4j
@RequestMapping("/test")
@RestController
public class TestController {
  @Resource
  UserTestService userTestService;

  @PostMapping(value = "/add")
  ApiResponse add(@RequestBody UserTest req) {
    // req.getUid();// 此处应添加：校验uid是否为家庭成员
    userTestService.addUserTest(req);
   return ApiResponse.ok();
  }

  @GetMapping(value = "/myList")
  ApiResponse myTest(WxAccount wxAccount) {
    return ApiResponse.ok(userTestService.findAllByUid(wxAccount.getId()));
  }

  @GetMapping(value = "/myFamilyList")
  ApiResponse myFamilyList(WxAccount wxAccount, Integer projectId) {
    return ApiResponse.ok(userTestService.myFamilyList(wxAccount.getId(), projectId));
  }

  @GetMapping(value = "/last")
  ApiResponse last(WxAccount wxAccount) {
    return ApiResponse.ok(userTestService.findLastByUid(wxAccount.getId()));
  }

}
