package com.example.demo.controller;

import com.example.demo.entity.UserInf;
import com.example.demo.service.UserInfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@Controller
public class LoginAndRegisterController {


    @Autowired
    UserInfService userInfService;

    /**
     *  登录
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String login(String username,String password, Model model,
                        HttpSession session) {
        // 通过账号和密码查询用户
        UserInf user = userInfService.verifyLogin(username,password);
        System.out.println(username+"----"+password);

        //验证用户
        if(user != null){
            // 将用户对象添加到Session
            session.setAttribute("USER_SESSION", user);
            // 跳转到主页面

            return "redirect:/main";
        }

        //验证失败
        model.addAttribute("msg", "账号或密码错误，请重新输入！");
        // 返回到登录页面
        System.out.println(username+"----"+password);
        return "login";

    }



    /**
     * 注册
     * */
    @ResponseBody
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public String toRgister(String username,String email,String password,Integer phone) {

        System.out.println("测试");
        //调用业务层注册方法
        Integer sign =  userInfService.insertSelective(username,email,password,phone);
        //判断是否注册成功，且向前端响应mesg
        if (sign != 0) {
            return "OK";
        }
        return "Fail";
    }

    /**
     * 退出登录
     */

    @RequestMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "login";
    }


}
