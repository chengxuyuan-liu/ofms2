package com.example.demo.controller;

import com.example.demo.entity.UserInf;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class MemberController {


    /*
    添加成员
    */
    @ResponseBody
    @RequestMapping("/addMember")
    public String addMember(Integer deptId,Integer UserPhone,HttpSession session){

        Boolean result = true;
        if(result) return "OK";
        return "FALSE";
    }
}
