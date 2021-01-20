package com.example.demo.config;
import com.example.demo.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class MyMvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("tologin").setViewName("login");
        registry.addViewController("menu").setViewName("menu");
        registry.addViewController("toregister").setViewName("register");
        registry.addViewController("dept_member").setViewName("dept_member");
        registry.addViewController("to_be_assigned").setViewName("dept_member");
        registry.addViewController("adminstrationMenu").setViewName("adminstration_menu");
        registry.addViewController("adminstrationUser").setViewName("adminstration_user");
        registry.addViewController("adminstration_login").setViewName("adminstration_login");
        registry.addViewController("forgot_password").setViewName("forgot_password");
        registry.addViewController("permission").setViewName("permission");
        registry.addViewController("adminstration_log").setViewName("adminstration_log");
        registry.addViewController("preview_error").setViewName("preview_error");
        registry.addViewController("video").setViewName("video");
        registry.addViewController("tariff").setViewName("tariff");
        registry.addViewController("tariff2").setViewName("tariff2");
        registry.addViewController("adminstration_order").setViewName("adminstration_order");
        registry.addViewController("adminstration_tariff").setViewName("adminstration_tariff");
        registry.addViewController("my_collect").setViewName("my_collect");
        registry.addViewController("my_distribute").setViewName("my_distribute");
        registry.addViewController("other_collect").setViewName("other_collect");
        registry.addViewController("other_distribute").setViewName("other_distribute");


    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**").excludePathPatterns("/userLogin",
                                                                                                    "login",
                                                                                                    "/error",
                                                                                                    "/register",
                                                                                                    "/adminstration_login",
                                                                                                    "/forGotPassword",
                                                                                                    "/forgot_password",
                                                                                                    "/adminstrationLogin",
                                                                                                    "/permission",
                                                                                                    "/checkEmail",
                                                                                                    "/**/*.js",
                                                                                                    "/**/*.css",
                                                                                                    "/**/*.jpg");
    }

}