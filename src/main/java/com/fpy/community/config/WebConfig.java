package com.fpy.community.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * 登录拦截
 */

//@EnableWebMvc //如果打开这个注解，则资源文件无法加载
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    SessionInteceptor sessionInteceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionInteceptor).addPathPatterns("/**");
    }
}
