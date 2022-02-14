package com.tencent.wxcloudrun.controller;

import com.alibaba.fastjson.JSONArray;
import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.CounterReqDto;
import com.tencent.wxcloudrun.model.Counter;
import com.tencent.wxcloudrun.model.WxAccount;
import com.tencent.wxcloudrun.service.CounterService;
import com.tencent.wxcloudrun.service.FamilyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * counter控制器
 */
@Slf4j
@RequestMapping("/family")
@RestController
public class FamilyController {
  @Resource
  FamilyService familyService;

  @GetMapping(value = "/memberlist")
  ApiResponse memberList(@SessionAttribute("wxAccount") WxAccount wxAccount) {
    return ApiResponse.ok(familyService.findAll(wxAccount.getId()));
  }

}
