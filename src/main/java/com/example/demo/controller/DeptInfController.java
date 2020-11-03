package com.example.demo.controller;

import com.example.demo.service.DeptInfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class DeptInfController {
    @Autowired
    DeptInfService deptInfService;

    @ResponseBody
    @RequestMapping("/getDept/{deptId}")
    public String selectByPrimaryKey(@PathVariable Integer deptId){
        String deptName = deptInfService.selectByPrimaryKey(1).getDeptName();
        System.out.println(deptName);
        return deptName;
    }



}
