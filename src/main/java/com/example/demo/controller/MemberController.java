package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.example.demo.entity.*;
import com.example.demo.service.*;
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
    UserInfService userInfService;
    @Autowired
    DeptMemberService deptMemberService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    FileCabinetService fileCabinetService;
    @Autowired
    PermissionService permissionService;
    @Autowired
    TeamService teamService;

    /*
    添加成员
    */
    @ResponseBody
    @RequestMapping("/addMember")
    public String addMember(Integer deptId,String userPhone,HttpSession session){

        //校验合法性
        //成员是否存在
        UserInf checkUser= userInfService.selectByUserPhone(userPhone);
        if(checkUser == null){
            return "用户不存在!";
        }
        //用户是否合法
        if(checkUser.getUserType() ==2){
            return "只能添加个人用户!";
        }
        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");
        //更新文件柜空间
        FileCabinet fileCabinet = fileCabinetService.selectByDeptId(deptId);
        Department department = departmentService.selectByPrimaryKey(deptId);
        //添加新成员
        DeptMember checkMember = deptMemberService.selectByUserKey(checkUser.getUserId());   //获取成员信息
        Team team = teamService.selectByPrimaryKey(department.getTeamId());   //获取成员所在的团队
        if(checkMember  == null){
            if(!fileCabinetService.updateWhenNewMember(fileCabinet)) return "文件柜空间已达上限!"; //文件空间已满
            Boolean result = deptMemberService.insertSelective(deptId,userPhone,userInf);
            return "添加成功!"; //添加成功
        }else if(checkMember  != null && checkMember.getTeamId().equals(team.getTeamId())){ //成员是否已加入团队
            return "该用户已存在团队中!";
        }else{
            return "用户已加入其他团队!"; //成员已存在
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
        inputMember.setUsedSpace(memberInfo.getUsedSpace());
        deptMemberService.updateByPrimaryKeySelective(inputMember); //更新成员信息，包括文件操作权限和空间
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
        record.setUsedSpace(UnitChange.TranslateGBtoByte(0));
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
