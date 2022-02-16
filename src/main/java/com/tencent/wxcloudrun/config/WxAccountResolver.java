package com.tencent.wxcloudrun.config;

import com.tencent.wxcloudrun.model.WxAccount;
import com.tencent.wxcloudrun.service.WxAppletService;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Component
public class WxAccountResolver implements HandlerMethodArgumentResolver {
    @Resource
    WxAppletService wxAppletService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return WxAccount.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String token = webRequest.getNativeRequest(HttpServletRequest.class).getHeader("authorization");
        return wxAppletService.getWxAccountByToken(token);
    }
}
