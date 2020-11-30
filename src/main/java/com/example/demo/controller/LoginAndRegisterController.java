package com.example.demo.controller;

import com.example.demo.entity.UserInf;
import com.example.demo.service.UserInfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;


@Controller
public class LoginAndRegisterController {
    @Autowired
    UserInfService userInfService;
    /*
     登录
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String login(String username,String password, Model model, HttpSession session) {
        // 通过账号和密码查询用户
        UserInf user = userInfService.verifyLogin(username,password);//验证登录信息

        System.out.println("账号："+user);
        //验证用户
        if(user != null){
            // 将用户对象添加到Session
            session.setAttribute("USER_SESSION", user);
            //跳转到管理页面
            if(user.getUserType() == 0) return "redirect:/adminstrationMenu";
            // 跳转到主页面
            return "redirect:/menu";
        }
        //验证失败
        model.addAttribute("msg", "账号或密码错误，请重新输入！");
        // 返回到登录页面
        System.out.println(username+"----"+password);
        return "login";
    }

    /*
 登录
 */
    @RequestMapping(value = "/adminstrationLogin",method = RequestMethod.POST)
    public String adminstrationLogin(String username,String password, Model model, HttpSession session) {
        // 通过账号和密码查询用户
        UserInf user = userInfService.verifyLogin(username,password);//验证登录信息

        System.out.println("账号："+user);
        //验证用户
        if(user != null && user.getUserType() == 0){
            // 将用户对象添加到Session
            session.setAttribute("USER_SESSION", user);
            //跳转到管理页面
            return "redirect:/adminstrationMenu";
        }
        //验证失败
        model.addAttribute("msg", "账号或密码错误，请重新确认！");
        // 返回到登录页面
        System.out.println(username+"----"+password);
        return "adminstration_login";
    }


    /*
     注册
     */
    @ResponseBody
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public String toRgister(Integer userType,String username,String email,String password,String phone) {

        System.out.println("用户类型："+userType+"用户名："+username+"邮箱："+email+"密码："+password+"手机："+phone);
        //调用业务层注册方法
        Integer sign =  userInfService.insertSelective(userType,username,email,password,phone);
        //判断是否注册成功，且向前端响应mesg
        if (sign != 0) {
            return "OK";
        }
        return "Fail";
    }
    /*
     退出登录
     */
    @RequestMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "login";
    }


}
