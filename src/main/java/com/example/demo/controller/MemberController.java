package com.example.demo.controller;

import com.example.demo.entity.DeptMember;
import com.example.demo.service.DeptMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.math.BigInteger;

@Controller
public class MemberController {


    @Autowired
    DeptMemberService deptMemberService;

    /*
    添加成员
    */
    @ResponseBody
    @RequestMapping("/addMember")
    public String addMember(Integer deptId,String userPhone,HttpSession session){
        //调用怎加成员业务 参数：部门Id、用户电话号码
        System.out.println("-----部门----"+deptId);
       Boolean result = deptMemberService.insertSelective(deptId,userPhone);
       if(result) return "OK";
       return "FALSE";
    }

    /*
    移除成员
    */
    @ResponseBody
    @RequestMapping("/removeMember")
    public String removeMember(Integer id,Integer deptId,HttpSession session){
        Boolean result = deptMemberService.updateDeptById(id,deptId);
        if(result) return "OK";
        return "FALSE";
    }

    /*
    删除成员
    */
    @ResponseBody
    @RequestMapping("/deleteMember")
    public String deleteMember(Integer id){
        int result = deptMemberService.deleteByPrimaryKey(id);
        if(result != 0) return "OK";
        return "FALSE";
    }

    /*
    编辑成员
    */
    @ResponseBody
    @RequestMapping(value = "/editMember",method = RequestMethod.POST)
    public String editMember(DeptMember member){
        //GB转字节（B）
        BigInteger num = new BigInteger("1024");
        BigInteger b = member.getMaxSpace().multiply(num).multiply(num).multiply(num);
        member.setMaxSpace(b);
        if(deptMemberService.updateByPrimaryKeySelective(member)) return "OK";
        return "FALSE";
    }




    /*
    待分配-编辑
    */
    @ResponseBody
    @RequestMapping("/editToBeAssignedMember")
    public String editToBeAssignedMember(DeptMember record) {
        record.setmStatus(1);
        record.setpUpload(0);
        record.setpPreview(0);
        record.setpDown(0);
        if (deptMemberService.updateByPrimaryKeySelective(record)) return "OK";
        return "FALSE";
    }



}
