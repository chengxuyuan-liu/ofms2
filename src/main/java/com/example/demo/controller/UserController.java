package com.example.demo.controller;


import com.example.demo.entity.Department;
import com.example.demo.entity.FileCabinet;
import com.example.demo.entity.UserInf;
import com.example.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
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
    public String deleteUser(Integer userId, HttpSession session){
        System.out.println("test");

        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");
        //删除用户部门和部门成员
        List<Department> departmentList = departmentService.selectDeptListByUserId(userId);

        if(departmentList.size()>0) {
            for (Department department : departmentList) {
                System.out.println("删除成员");
                deptMemberService.deleteByDeptId(department.getDeptId());
            }
        }
        //deptMemberService.


        //删除用户文件和文件夹
        fileInfServive.deleteByUserId(userId);  //删除文件
        dirInfService.deleteByUserId(userId);   //删除文件夹

        int result = userInfService.deleteByPrimaryKey(userId); //删除用户

        return result != 0? "OK" : "FALSE";

    }


    @ResponseBody
    @RequestMapping("/editUser")
    public String editUser(Integer userId,String maxSpace,HttpSession session){
        System.out.println(userId+"-----"+maxSpace);
        UserInf userInf = new UserInf();
        userInf.setUserId(userId);
        userInf.setMaxSpace(new BigInteger(maxSpace));
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




}
