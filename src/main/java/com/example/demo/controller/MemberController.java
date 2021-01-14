package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.example.demo.entity.*;
import com.example.demo.service.DepartmentService;
import com.example.demo.service.DeptMemberService;
import com.example.demo.service.FileCabinetService;
import com.example.demo.service.PermissionService;
import com.example.demo.util.UnitChange;
import com.example.demo.vo.MemberVO;
import com.example.demo.vo.PermissionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Controller
public class MemberController {


    @Autowired
    DeptMemberService deptMemberService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    FileCabinetService fileCabinetService;
    @Autowired
    PermissionService permissionService;

    /*
    添加成员
    */
    @ResponseBody
    @RequestMapping("/addMember")
    public String addMember(Integer deptId,String userPhone,HttpSession session){

        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");
        //更新文件柜空间
        FileCabinet fileCabinet = fileCabinetService.selectByDeptId(deptId);
        //添加新成员
        try {
            Boolean result = deptMemberService.insertSelective(deptId,userPhone,userInf);
            if(!fileCabinetService.updateWhenNewMember(fileCabinet)) return "FILE_CABINET_FULL";
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

        System.out.println(deptMember.getId());
        FileCabinet fileCabinet = fileCabinetService.selectByDeptId(deptMember.getDeptId());
        fileCabinetService.updateWhenDeleteMember(fileCabinet,deptMember);
        //移除角色
        Permission permission = permissionService.selectByMemberId(deptMember.getId());
        if(permission != null){
            permissionService.deleteByMemberId(deptMember.getId());
        }

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
    * 批量删除成员
    * */
    @ResponseBody
    @RequestMapping("/deleteBatchMember")
    public String deleteBatchMember(String[] memberId){
        if (memberId != null) {
            for (int i = 0; i < memberId.length; i++) {
                deptMemberService.deleteByPrimaryKey(Integer.parseInt(memberId[i]));
            }
        }
        return "OK";
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

    /*
     * 获取成员
     * */
    @ResponseBody
    @RequestMapping("/getMember")
    public String getMember(Map<String,Object> map, Integer deptId,HttpSession session) {
        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");

        List<MemberVO> memberVOS = deptMemberService.selectListByDeptKey(deptId);
        //删除已经存在权限的成员
        List<PermissionVO> permissionVOList = permissionService.selectByUserId(userInf.getUserId());
        Iterator<MemberVO> di = memberVOS.iterator();

        //删除已有角色的成员
        while(di.hasNext()){
            MemberVO memberVO = di.next();
            for (PermissionVO permissionVO : permissionVOList) {
                if(memberVO.getId().equals(permissionVO.getMemberId())){
                    di.remove();
                }
            }
        }


        String str = JSONArray.toJSONString(memberVOS);
        return str;
    }


    /*
     * 批量移除成员
     * */
    @ResponseBody
    @RequestMapping("/removeBatchMember")
    public String removeBatchMember(String[] memberId,Map<String,Object> map) {

        System.out.println("批量删除");
        //批量删除
        if (memberId != null) {
            for (int i = 0; i < memberId.length; i++) {
                //更新文件柜
                DeptMember deptMember = deptMemberService.selectByPrimaryKey(Integer.parseInt(memberId[i]));

                System.out.println(deptMember.getId());
                FileCabinet fileCabinet = fileCabinetService.selectByDeptId(deptMember.getDeptId());
                fileCabinetService.updateWhenDeleteMember(fileCabinet,deptMember);
                //移除角色
                Permission permission = permissionService.selectByMemberId(deptMember.getId());
                if(permission != null){
                    permissionService.deleteByMemberId(deptMember.getId());
                }
                //移除成员
                Boolean result = deptMemberService.updateDeptById(Integer.parseInt(memberId[i]),null);

            }
        }





        return "OK";
    }





}
