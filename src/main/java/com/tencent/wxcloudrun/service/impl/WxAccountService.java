package com.tencent.wxcloudrun.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.tencent.wxcloudrun.config.JwtConfig;
import com.tencent.wxcloudrun.dao.FamilyMapper;
import com.tencent.wxcloudrun.dao.WxAccountMapper;
import com.tencent.wxcloudrun.dto.Code2SessionResponse;
import com.tencent.wxcloudrun.dto.CodeReqDto;
import com.tencent.wxcloudrun.dto.TokenDto;
import com.tencent.wxcloudrun.dto.UpdateWxAccountReqDto;
import com.tencent.wxcloudrun.model.ApiException;
import com.tencent.wxcloudrun.model.Family;
import com.tencent.wxcloudrun.model.WxAccount;
import com.tencent.wxcloudrun.service.FamilyService;
import com.tencent.wxcloudrun.service.WxAppletService;
import com.tencent.wxcloudrun.utils.HttpUtils;
import com.tencent.wxcloudrun.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
@Slf4j
@Service
public class WxAccountService implements WxAppletService {

    @Value("${wx.applet.appid}")
    private String appid;
    @Value("${wx.applet.appsecret}")
    private String appSecret;

    @Resource
    private WxAccountMapper wxAccountMapper;
    @Resource
    private FamilyService familyService;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private JwtConfig jwtConfig;


    /**
     * 微信的 code2session 接口 获取微信用户信息
     * 官方说明 : https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/login/auth.code2Session.html
     */
    private String code2Session(String jsCode) {
        String code2SessionUrl = "https://api.weixin.qq.com/sns/jscode2session";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("appid", appid);
        params.add("secret", appSecret);
        params.add("js_code", jsCode);
        params.add("grant_type", "authorization_code");
        URI code2Session = HttpUtils.getURIwithParams(code2SessionUrl, params);
        return restTemplate.exchange(code2Session, HttpMethod.GET, new HttpEntity<String>(new HttpHeaders()), String.class).getBody();
    }


    /**
     * 微信小程序用户登陆，完整流程可参考下面官方地址，本例中是按此流程开发
     * https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/login.html
     *
     * @param req 小程序端 调用 wx.login 获取到的code,用于调用 微信code2session接口
     * @return 返回后端 自定义登陆态 token  基于JWT实现
     */
    @Override
    public TokenDto wxUserLogin(CodeReqDto req) {
        //1 . code2session返回JSON数据
        String resultJson = code2Session(req.getCode());
        //2 . 解析数据
        Code2SessionResponse response = JsonUtils.toJavaObject(resultJson, Code2SessionResponse.class);
//        log.info("resultJson:" + resultJson);
//        log.info("response:" + response);
        if ("0".equals(response.getErrcode())) {
            //3 . 先从本地数据库中查找用户是否存在
            WxAccount wxAccount = wxAccountMapper.findByWxOpenid(response.getOpenid());
            if (wxAccount == null) {
                wxAccount = new WxAccount();
                wxAccount.setOpenid(response.getOpenid());    //不存在就新建用户
            }
            wxAccount.setUnionid(response.getUnionid());
            //4 . 更新sessionKey和 登陆时间
            wxAccount.setSessionKey(response.getSession_key());
            wxAccountMapper.upsertWxAccount(wxAccount);
            //5 . JWT 返回自定义登陆态 Token

            Integer uid = wxAccount.getId();
            if (uid == 0) {
                // 获取id，用户昵称等
                wxAccount = wxAccountMapper.findByWxOpenid(wxAccount.getOpenid());
            }

            //5 . JWT 返回自定义登陆态 Token
            String token = jwtConfig.createTokenByWxAccount(wxAccount);

            try {
                Integer headUid = familyService.getFamilyHeadUid(wxAccount.getId(), false);
                wxAccount.setHeadUid(headUid);
                return new TokenDto(token, uid, wxAccount.getHeadUid(), wxAccount.getNickName(), wxAccount.getAvatarUrl(), wxAccount.getRole(), wxAccount.getGender());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public WxAccount getWxAccountByToken(String token) {
        if (!jwtConfig.verifyToken(token)) {
            throw new ApiException("Token Invalid");
        }
        WxAccount wxAccount = jwtConfig.getWxAccountByToken(token);
        Integer headUid = familyService.getFamilyHeadUid(wxAccount.getId(), false);
        wxAccount.setHeadUid(headUid);
        return wxAccount;
    }

    @Override
    public void updateWxAccount(UpdateWxAccountReqDto req) {
        WxAccount wxAccount = wxAccountMapper.findByWxUid(req.getId());

        if (StringUtils.isNotBlank(req.getNickName())) {
            wxAccount.setNickName(req.getNickName());
        }
        if (StringUtils.isNotBlank(req.getAvatarUrl())) {
            wxAccount.setAvatarUrl(req.getAvatarUrl());
        }
        if (StringUtils.isNotBlank(req.getMobile())) {
            wxAccount.setMobile(req.getMobile());
        }

        if (req.getGender() > 0) {
            wxAccount.setGender(req.getGender()); //性别 0：未知、1：男、2：女
        }

        if (StringUtils.isNotBlank(req.getCountry())) {
            wxAccount.setCountry(req.getCountry());
        }
        if (StringUtils.isNotBlank(req.getProvince())) {
            wxAccount.setProvince(req.getProvince());
        }
        if (StringUtils.isNotBlank(req.getCity())) {
            wxAccount.setCity(req.getCity());
        }

        wxAccountMapper.updateById(wxAccount);
    }

}
