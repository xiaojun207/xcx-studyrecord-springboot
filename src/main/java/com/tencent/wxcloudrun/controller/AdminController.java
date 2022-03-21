package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.model.WxAccount;
import com.tencent.wxcloudrun.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.ConstructorArgs;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * counter控制器
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminController {

    final AdminService adminService;

    /**
     * 获取当前计数
     *
     * @return API response json
     */
    @GetMapping(value = "/info")
    ApiResponse get(WxAccount wxAccount) {
        if (!wxAccount.isAdmin()) {
            log.error("异常访问：" + wxAccount);
            ApiResponse.error("访问异常");
        }
        return ApiResponse.ok(adminService.getSystemInfo());
    }


}
