package com.tencent.wxcloudrun.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.tencent.wxcloudrun.model.WxAccount;
import com.tencent.wxcloudrun.service.PushMsgService;
import com.tencent.wxcloudrun.utils.ExpiryMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Slf4j
@Service
public class PushMsgServiceImpl implements PushMsgService {
    @Resource
    private RestTemplate restTemplate;

    @Value("${wx.applet.appid}")
    private String appid;
    @Value("${wx.applet.appsecret}")
    private String appSecret;

    ExpiryMap<String, String> accessTokenMap = new ExpiryMap<>();

    /**
     * https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/access-token/auth.getAccessToken.html
     * @return
     */
    public String getAccessToken(){
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appid+ "&secret=" + appSecret;
        JSONObject result = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(new HttpHeaders()), JSONObject.class).getBody();
        log.info("pushMsgToUser.result:" + result);
        accessTokenMap.put("access_token", result.getString("access_token"), 7200 * 1000);
        return result.getString("access_token");
    }

    /**
     * https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/subscribe-message/subscribeMessage.send.html#method-http
     * @param to
     * @param msg
     */
    @Override
    public void pushMsgToUser(WxAccount to, String title,  String msg) {
       String accessToken = accessTokenMap.get("access_token");
        if(accessToken == null || accessToken.isEmpty()){
            accessToken = this.getAccessToken();
        }

        String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + accessToken;


        JSONObject data = new JSONObject();

        JSONObject name1 = new JSONObject();
        name1.put("value", "@" + to.getNickName());
        data.put("name1", name1);

        JSONObject thing2 = new JSONObject();
        thing2.put("value", title);
        data.put("thing2", thing2);

        JSONObject thing3 = new JSONObject();
        thing3.put("value", msg);
        data.put("thing3", thing3);

        JSONObject body = new JSONObject();
        body.put("touser", to.getOpenid());
        body.put("template_id", "AIdOp_kCAVABOVtY7UL-dlkJPDxp_fT15Q6pweG_qiA");
        body.put("page", "/pages/index/index");
        body.put("data", data);

        log.info("body:" + body);
        String result = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<JSONObject>(body, new HttpHeaders()), String.class).getBody();
        log.info("pushMsgToUser.result:" + result);
    }
}
