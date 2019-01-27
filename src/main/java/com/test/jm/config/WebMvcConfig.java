package com.test.jm.config;

import com.test.jm.config.interceptors.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Autowired
    private LoginInterceptor loginInterceptor;


    // 这个方法是用来配置静态资源的，比如html，js，css，等等
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
    }

    // 这个方法用来注册拦截器，我们自己写好的拦截器需要通过这里添加注册才能生效
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/user/test")
                .addPathPatterns("/api/**")
                .addPathPatterns("/project/**")
                .addPathPatterns("/case/**")
                .addPathPatterns("/task/**")
                .addPathPatterns("/job/**")
                .excludePathPatterns("/user/login", "/user/register","/user/2login");
        super.addInterceptors(registry);
    }

    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.set(0,new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }
}
