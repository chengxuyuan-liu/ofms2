package com.example.demo.controller;


import com.example.demo.entity.DirInf;
import com.example.demo.entity.FileCabinet;
import com.example.demo.entity.UserInf;
import com.example.demo.service.*;
import com.example.demo.util.UnitChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

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
    public String editUser(Integer userId,Integer perSpace,Integer teamSpace,HttpSession session){

        //个人文件柜
        DirInf rootDirInf = dirInfService.selectRootDirByUserId(userId);//根文件夹
        DirInf myDir = dirInfService.selectDirByDirName("我的文件",rootDirInf.getDirId()); //'我的文件'
        FileCabinet fileCabinet = fileCabinetService.selectByDirId(myDir.getDirId());
        FileCabinet newFileCabinet = new FileCabinet();
        newFileCabinet.setMaxSpace(UnitChange.TranslateGBtoByte(perSpace));
        newFileCabinet.setFcId(fileCabinet.getFcId());
        fileCabinetService.updateByPrimaryKeySelective(fileCabinet.getFcId(), null, UnitChange.TranslateGBtoByte(perSpace), null, null);
        //团队空间
        int result=0;
        if(teamSpace != null) {
            UserInf userInf = new UserInf();
            userInf.setUserId(userId);
            userInf.setMaxSpace(UnitChange.TranslateGBtoByte(teamSpace));
            result = userInfService.updateByPrimaryKeySelective(userInf);
        }


        return result != 0? "OK" : "FALSE";
    }

    @ResponseBody
    @RequestMapping("/forGotPassword")
    public String forGotPassword(String email) {
        //通过邮箱查询用户信息
       UserInf userInf = userInfService.selectByEmail(email);
        System.out.println(userInf.getPassword());
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


    /*
    * 修改密码
    * */
    @ResponseBody
    @RequestMapping("/changePassword")
    public String changePassword(String password,HttpSession session) {
        UserInf nowUser = (UserInf) session.getAttribute("USER_SESSION");
        //通过邮箱查询用户信息
        UserInf userInf = new UserInf();
        userInf.setUserId(nowUser.getUserId());
        userInf.setPassword(password);
        int result = userInfService.updateByPrimaryKeySelective(userInf);
        if(result == 0){
            return "修改失败！";
        }
        return "修改成功！";
    }


    //禁用用户
    @ResponseBody
    @RequestMapping("/disableUser")
    public String disableUser(Integer userId,HttpSession session) {

        UserInf tagetUser = userInfService.selectByPrimaryKey(userId);
        UserInf userInf = new UserInf();
        userInf.setUserId(userId);
        if(tagetUser.getStatus() == 1){
            userInf.setStatus(0);
        }else{
            userInf.setStatus(1);
        }

        userInfService.updateByPrimaryKeySelective(userInf);
        return "修改成功!";
    }

}
