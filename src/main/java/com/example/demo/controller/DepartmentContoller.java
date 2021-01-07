package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.service.*;
import com.example.demo.util.UnitChange;
import com.example.demo.vo.MemberVO;
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
    @Autowired
    PermissionService permissionService;

    /*
    * 新建部门
    * */
    @ResponseBody
    @RequestMapping("/newDept")
    public String newDept(Department dept, HttpSession session){
        //当前用户
        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");

        Team team;
        if(userInf.getUserType() == 2) {
            //调用创建部门service
            team = teamService.selectByUserId(userInf);
        }else{
            DeptMember deptMember = deptMemberService.selectByUserKey(userInf.getUserId());
            team =  teamService.selectByPrimaryKey(deptMember.getTeamId());
        }

        UserInf teamUser = userInfService.selectByPrimaryKey(team.getUserId()); //获得团队所属人

        //判断空间是否足够
        if(!userInfService.judgeSpace(teamUser)) return "SPACE_FULL";

        //调用创建文件夹service
        DirInf dirInf = dirInfService.selectRootDirByUserId(teamUser.getUserId()); //获得根目录
        DirInf newDeptDir = dirInfService.insertSelective(dept.getDeptName(),dirInf.getDirId(),teamUser); //创建文件夹

        //调用创建文件柜service
        FileCabinet fileCabinet = fileCabinetService.newFileCabinet(dept,teamUser,newDeptDir);


        departmentService.insertSelective(fileCabinet,team);

        //更新用户空间
        userInfService.updateSpaceWhenNewDept(teamUser);

        UserInf newUser = userInfService.selectByPrimaryKey(userInf.getUserId());

        session.setAttribute("USER_SESSION",newUser);
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

        List<MemberVO> memberVOS = deptMemberService.selectListByUserId(deptId);

        //删除角色
        for (MemberVO memberVO : memberVOS) {
            Permission permission = permissionService.selectByMemberId(memberVO.getId());
            if(permission != null){
                permissionService.deleteByPrimaryKey(permission.getPsiId());
            }
        }
        //permissionService.deleteByMemberId()


        //删除部门
        departmentService.deleteByPrimaryKey(deptId);
        //删除文件夹,级联删除文件柜和文件
        FileCabinet fileCabinet = fileCabinetService.selectByDeptId(deptId);
        dirInfService.deleteByPrimaryKey(fileCabinet.getDirId());
        //改变用户总空间
        userInfService.updateSpaceWhenDeleteDept(userInf,fileCabinet);

        UserInf newUser = userInfService.selectByPrimaryKey(userInf.getUserId());
        session.setAttribute("USER_SESSION",newUser);
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
