package com.example.demo.controller;


import com.example.demo.entity.Department;
import com.example.demo.entity.FileCabinet;
import com.example.demo.entity.UserInf;
import com.example.demo.service.*;
import com.example.demo.util.UnitChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    UserInfService userInfService;
    @Autowired
    FileCabinetService fileCabinetService;
    @Autowired
    DeptMemberService deptMemberService;
    @Autowired
    DirInfService dirInfService;
    @Autowired
    FileInfServive fileInfServive;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    DepartmentService departmentService;


    //删除用户
    @ResponseBody
    @RequestMapping("/deleteUser")
    public String deleteUser(String[] userId, HttpSession session){


        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");
        //删除用户部门和部门成员
        for (int i = 0; i < userId.length; i++) {
            userInfService.deleteByPrimaryKey(Integer.parseInt(userId[i])); //删除用户
        }


        return "OK";

    }



    /*
    * 编辑用户
    * */
    @ResponseBody
    @RequestMapping("/editUser")
    public String editUser(Integer userId,Integer maxSpace,HttpSession session){
        UserInf userInf = new UserInf();
        userInf.setUserId(userId);
        userInf.setMaxSpace(UnitChange.TranslateGBtoByte(maxSpace));
        int result = userInfService.updateByPrimaryKeySelective(userInf);
        return result != 0? "OK" : "FALSE";
    }

    @ResponseBody
    @RequestMapping("/forGotPassword")
    public String forGotPassword(String email) {
        //通过邮箱查询用户信息
       UserInf userInf = userInfService.selectByEmail(email);

       if(userInf != null){
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("2919586256@qq.com");
            message.setTo(userInf.getEmail());
            message.setSubject("办公文件管理系统----找回密码");
            message.setText("账号："+userInf.getEmail()+"     "+"密码:"+userInf.getPassword());
            javaMailSender.send(message);
            return "OK";
        }
        return "FALSE";
    }

    @ResponseBody
    @RequestMapping("/checkEmail")
    public String checkEmail(String email) {
        //通过邮箱查询用户信息
        UserInf userInf = userInfService.selectByEmail(email);
        if(userInf != null){
            return "OK";
        }
        return "FALSE";

    }

    @ResponseBody
    @RequestMapping("/changePassword")
    public String changePassword(String password,HttpSession session) {

        UserInf nowUser = (UserInf) session.getAttribute("USER_SESSION");
        //通过邮箱查询用户信息
        UserInf userInf = new UserInf();
        userInf.setUserId(nowUser.getUserId());
        userInf.setPassword(password);
        int result = userInfService.updateByPrimaryKeySelective(userInf);
        if(result != 0){
            return "OK";
        }
        return "FALSE";

    }




}
