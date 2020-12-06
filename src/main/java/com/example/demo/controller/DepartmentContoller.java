package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.service.*;
import com.example.demo.util.UnitChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.util.List;

@Controller
public class DepartmentContoller {


    @Autowired
    FileCabinetService fileCabinetService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    DirInfService dirInfService;
    @Autowired
    TeamService teamService;
    @Autowired
    UserInfService userInfService;
    @Autowired
    DeptMemberService deptMemberService;
    @Autowired
    FileInfServive fileInfServive;

    /*
    * 新建部门
    * */
    @ResponseBody
    @RequestMapping("/newDept")
    public String newDept(Department dept, HttpSession session){
        //当前用户
        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");

        //判断空间是否足够
        if(!userInfService.judgeSpace(userInf)) return "SPACE_FULL";

        //调用创建文件夹service
        DirInf dirInf = dirInfService.selectRootDirByUserId(userInf.getUserId()); //获得根目录
        DirInf newDeptDir = dirInfService.insertSelective(dept.getDeptName(),dirInf.getDirId(),userInf); //创建文件夹

        //调用创建文件柜service
        FileCabinet fileCabinet = fileCabinetService.newFileCabinet(dept,userInf,newDeptDir);

        //调用创建部门service
        Team team = teamService.selectByUserId(userInf);
        departmentService.insertSelective(fileCabinet,team);

        //更新用户空间
        userInfService.updateSpaceWhenNewDept(userInf);

        return "OK";
    }


    /*
    * 解散部门
    * */
    @ResponseBody
    @RequestMapping("/deleteDept")
    public String deleteDept(Integer deptId,HttpSession session){
        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");
        //解散成员
        deptMemberService.dissolveMemberToBeAssigned(deptId);
        //删除部门
        departmentService.deleteByPrimaryKey(deptId);
        //删除文件夹,级联删除文件柜和文件
        FileCabinet fileCabinet = fileCabinetService.selectByDeptId(deptId);
        dirInfService.deleteByPrimaryKey(fileCabinet.getDirId());
        //改变用户总空间
        userInfService.updateSpaceWhenDeleteDept(userInf,fileCabinet);
        return "OK";
    }


    /*
    * 编辑部门
    * */
    @ResponseBody
    @RequestMapping("/editDept")
    public String editDept(String deptName,Integer maxSpace,Integer deptId,HttpSession session){
        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");
        BigInteger maxSpaceChange = UnitChange.TranslateGBtoByte(maxSpace); //转换单位
        //调用业务
        //更新部门（名字 和 空间）
        departmentService.updateByPrimaryKeySelective(deptId,deptName,null,null);
        //更新用户空间
        FileCabinet fileCabinet = fileCabinetService.selectByDeptId(deptId);
        userInfService.updateSpaceWhenEditDept(userInf,fileCabinet,maxSpaceChange);
        //更新文件柜
        fileCabinetService.updateByPrimaryKeySelective(fileCabinet.getFcId(),deptName,maxSpaceChange,null,null);
        return "OK";
    }
}
