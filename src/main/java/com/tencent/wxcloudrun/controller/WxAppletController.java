package com.tencent.wxcloudrun.controller;

import com.alibaba.fastjson.JSONObject;
import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.CodeReqDto;
import com.tencent.wxcloudrun.dto.TokenDto;
import com.tencent.wxcloudrun.dto.UpdateWxAccountReqDto;
import com.tencent.wxcloudrun.model.WxAccount;
import com.tencent.wxcloudrun.service.WxAppletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
public class WxAppletController {

    @Resource
    WxAppletService wxAppletService;

    /**
     * 微信小程序端用户登陆api
     * 返回给小程序端 自定义登陆态 token
     */
    @PostMapping("/wx/user/login")
    ApiResponse wxAppletLoginApi(@RequestBody @Validated CodeReqDto req) {
        TokenDto token = wxAppletService.wxUserLogin(req);
        if (token != null) {
            return ApiResponse.ok(token);
        }
        return ApiResponse.error("login error");
    }

    @PostMapping("/wx/msg/receive")
    public String wxMsgReceive(@RequestBody JSONObject req) {
        log.info("wxMsgReceive:" + req);
        return "success";
    }
    /**
     * 需要认证的测试接口  需要 @RequiresAuthentication 注解，则调用此接口需要 header 中携带自定义登陆态 authorization
     */
    @PostMapping("/sayHello")
    ApiResponse sayHello(WxAccount wxAccount) {
        log.info("wxAccount:" + wxAccount);
        Map<String, String> result = new HashMap<>();
        result.put("words", "hello World");
        return ApiResponse.ok(result);
    }

    @PostMapping("/wx/user/update")
    ApiResponse updateUserInfo(WxAccount wxAccount, @RequestBody @Validated UpdateWxAccountReqDto req) {
        req.setId(wxAccount.getId());
        req.setOpenid(wxAccount.getOpenid());
        wxAppletService.updateWxAccount(req);
        return ApiResponse.ok();
    }

}
