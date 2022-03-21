package com.tencent.wxcloudrun.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tencent.wxcloudrun.dao.UserTestMapper;
import com.tencent.wxcloudrun.dao.WxAccountMapper;
import com.tencent.wxcloudrun.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdminServiceImpl implements AdminService {
    final WxAccountMapper wxAccountMapper;
    final UserTestMapper userTestMapper;

    @Override
    public JSONObject getSystemInfo() {
        Long userCount = wxAccountMapper.selectCount(new QueryWrapper<>());
        Long userTestCount = userTestMapper.selectCount(new QueryWrapper<>());

        JSONObject res = new JSONObject();
        res.put("userCount", userCount);
        res.put("userTestCount", userTestCount);
        return res;
    }
}
