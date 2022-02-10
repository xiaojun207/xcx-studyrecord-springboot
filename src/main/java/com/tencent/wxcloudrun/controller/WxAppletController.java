package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.CodeReqDto;
import com.tencent.wxcloudrun.dto.TokenDto;
import com.tencent.wxcloudrun.model.WxAccount;
import com.tencent.wxcloudrun.service.WxAppletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


/**
 * 小程序后台 某 API
 */
@Slf4j
@RestController
public class WxAppletController {

    @Resource
    private WxAppletService wxAppletService;

    /**
     * 微信小程序端用户登陆api
     * 返回给小程序端 自定义登陆态 token
     */
    @PostMapping("/api/wx/user/login")
    public ApiResponse wxAppletLoginApi(@RequestBody @Validated CodeReqDto request) {
        TokenDto token = wxAppletService.wxUserLogin(request.getCode());
        if (token != null) {
            return ApiResponse.ok(token);
        }
        return ApiResponse.error("login error");
    }

    /**
     * 需要认证的测试接口  需要 @RequiresAuthentication 注解，则调用此接口需要 header 中携带自定义登陆态 authorization
     */
//    @RequiresAuthentication
    @PostMapping("/sayHello")
    public ResponseEntity sayHello(@SessionAttribute WxAccount wxAccount) {
        log.info("wxAccount:" + wxAccount);
        Map<String, String> result = new HashMap<>();
        result.put("words", "hello World");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
