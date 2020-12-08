package com.example.demo.controller;

import com.example.demo.entity.Department;
import com.example.demo.entity.DeptMember;
import com.example.demo.entity.FileCabinet;
import com.example.demo.entity.UserInf;
import com.example.demo.service.DepartmentService;
import com.example.demo.service.DeptMemberService;
import com.example.demo.service.FileCabinetService;
import com.example.demo.util.UnitChange;
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
    @Autowired
    DepartmentService departmentService;
    @Autowired
    FileCabinetService fileCabinetService;

    /*
    添加成员
    */
    @ResponseBody
    @RequestMapping("/addMember")
    public String addMember(Integer deptId,String userPhone,HttpSession session){

        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");
        //更新文件柜空间
        FileCabinet fileCabinet = fileCabinetService.selectByDeptId(deptId);
        if(!fileCabinetService.updateWhenNewMember(fileCabinet)) return "FILE_CABINET_FULL";
        //添加新成员
       try {
           Boolean result = deptMemberService.insertSelective(deptId,userPhone,userInf);
           return "OK";
       }catch (Exception e){
           return "MEMBER_EXIST";
       }

    }

    /*
    移除成员
    */
    @ResponseBody
    @RequestMapping("/removeMember")
    public String removeMember(Integer id,Integer deptId,HttpSession session){

        //更新文件柜
        DeptMember deptMember = deptMemberService.selectByPrimaryKey(id);
        FileCabinet fileCabinet = fileCabinetService.selectByDeptId(deptMember.getDeptId());
        fileCabinetService.updateWhenDeleteMember(fileCabinet,deptMember);

        //移除成员
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
    public String editMember(DeptMember inputMember){
        //单位转换
        inputMember.setMaxSpace(UnitChange.TranslateMBtoByte(inputMember.getMaxSpace().intValue()));
        //更新文件柜空间
        DeptMember memberInfo =deptMemberService.selectByPrimaryKey(inputMember.getId());
        FileCabinet fileCabinet = fileCabinetService.selectByDeptId(memberInfo.getDeptId());
        if(!fileCabinetService.updateWhenEditMember(fileCabinet,memberInfo,inputMember.getMaxSpace())) return "FILE_CABINET_FULL";
        //更新成员空间
        deptMemberService.updateByPrimaryKeySelective(inputMember);
        return "OK";
    }




    /*
    待分配-编辑
    */
    @ResponseBody
    @RequestMapping("/editToBeAssignedMember")
    public String editToBeAssignedMember(DeptMember record) {
        //单位转换
        BigInteger count = UnitChange.TranslateMBtoByte(record.getMaxSpace().intValue());
        //更新文件柜
        FileCabinet fileCabinet = fileCabinetService.selectByDeptId(record.getDeptId());
        System.out.println(fileCabinet.getFcName());
        BigInteger countFC = fileCabinet.getUsedSpace().add(count);
        System.out.println(countFC);
        fileCabinetService.updateByPrimaryKeySelective(fileCabinet.getFcId(),null,null,countFC,null);


        //更新部门成员
        record.setmStatus(1);
        record.setpUpload(1);
        record.setpPreview(1);
        record.setpDown(1);

        record.setMaxSpace(count);
        if (deptMemberService.updateByPrimaryKeySelective(record)) return "OK";
        return "FALSE";
    }

    /*
    * 获取部门id
    * */
    @ResponseBody
    @RequestMapping("/getDeptId")
    public String getDeptId(Integer deptId) {

        Department department = departmentService.selectByFileCabinetId(deptId);
        System.out.println(department.getDeptId());
        return department.getDeptId().toString();
    }




}
