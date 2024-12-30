package com.shop.user_service.config;

import common.annotation.UserIdMethodArgumentResolver;
import common.filter.GateWayFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {


    UserIdMethodArgumentResolver userIdMethodArgumentResolver = new UserIdMethodArgumentResolver();

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userIdMethodArgumentResolver);
    }

//    @Bean
//    public FilterRegistrationBean<GateWayFilter> myFilterRegistration() {
//        FilterRegistrationBean<GateWayFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(new GateWayFilter());
//        registrationBean.addUrlPatterns("/**"); //全部都拦截，因为要检测是否来自网关
//        registrationBean.setName("GateWayFilter");
//        registrationBean.setOrder(-1); // 过滤器的优先级
//        return registrationBean;
//    }

    @Bean
    public FilterRegistrationBean<GateWayFilter> myFilterRegistration(){
        FilterRegistrationBean<GateWayFilter> filterRegistrationBean = new FilterRegistrationBean<>(new GateWayFilter());
        String[] urlPatterns = {
                "/*"
        };
        filterRegistrationBean.addUrlPatterns(urlPatterns);
        filterRegistrationBean.setOrder(-1);
        return filterRegistrationBean;
    }


}
