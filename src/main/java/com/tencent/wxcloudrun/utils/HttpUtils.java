package com.tencent.wxcloudrun.utils;

import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class HttpUtils {
    /**
     * 根据url和请求参数获取URI
     */
    public static URI getURIwithParams(String url, MultiValueMap<String, String> params) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParams(params);
        return builder.build().encode().toUri();
    }
}
