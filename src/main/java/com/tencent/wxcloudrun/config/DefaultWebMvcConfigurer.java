package com.tencent.wxcloudrun.config;

import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

@Component
public class DefaultWebMvcConfigurer implements WebMvcConfigurer { // extends WebMvcConfigurerAdapter

    @Resource
    WxAccountResolver wxAccountResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(wxAccountResolver);
    }

}
