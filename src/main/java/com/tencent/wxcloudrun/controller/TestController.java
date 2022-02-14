package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.CounterReqDto;
import com.tencent.wxcloudrun.model.Counter;
import com.tencent.wxcloudrun.model.UserTest;
import com.tencent.wxcloudrun.model.WxAccount;
import com.tencent.wxcloudrun.service.CounterService;
import com.tencent.wxcloudrun.service.UserTestService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Optional;

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

  @GetMapping(value = "/mylist")
  ApiResponse myTest(@SessionAttribute("wxAccount") WxAccount wxAccount) {
    return ApiResponse.ok(userTestService.findAllByUid(wxAccount.getId()));
  }

}
