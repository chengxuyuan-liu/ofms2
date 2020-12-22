package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.example.demo.entity.*;
import com.example.demo.service.PermissionService;
import com.example.demo.vo.PermissionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class PermissionController {

    @Autowired
    PermissionService permissionService;

    /*
     * 添加新角色
     * */
    @ResponseBody
    @RequestMapping("/newRole")
    public String newRole(Permission role, HttpSession session){
        //当前用户
        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");
        //添加权限
        permissionService.insertSelective(role);
        return "OK";
    }

    /*
     * 删除角色
     * */
    @ResponseBody
    @RequestMapping("/deleteRole")
    public String deleteRole(Integer psiId, HttpSession session){
        //当前用户
        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");
        //删除权限
        permissionService.deleteByPrimaryKey(psiId);
        return "OK";
    }

    /*
     * 编辑角色
     * */
    @ResponseBody
    @RequestMapping("/editRole")
    public String editRole(Permission role, HttpSession session){

        System.out.println(role.toString());
        //当前用户
        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");
        //删除权限
        permissionService.updateByPrimaryKeySelective(role);
        return "OK";
    }


    /*
     * 获得角色
     * */
    @ResponseBody
    @RequestMapping("/getRole")
    public String getRole(Integer psiId){
        //删除权限
        PermissionVO permissionVO = permissionService.selectByPsiId(psiId);
        String str = JSONArray.toJSONString(permissionVO);
        return str;
    }



}
