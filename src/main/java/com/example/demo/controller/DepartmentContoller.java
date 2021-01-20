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
        //获得当前所在团队和团队负责人id
        Team team;
        if(userInf.getUserType() == 2) {
            team = teamService.selectByUserId(userInf);
        }else{
            DeptMember deptMember = deptMemberService.selectByUserKey(userInf.getUserId());
            team =  teamService.selectByPrimaryKey(deptMember.getTeamId());
        }
        //判断重名
        Department department = departmentService.selectRepeatDeptName(dept,team);
        if (department != null) {
            System.out.println("校验合法性");
            return "部门已存在！";
        }
        UserInf teamUser = userInfService.selectByPrimaryKey(team.getUserId()); //获得团队负责人信息
        //判断空间是否足够
        if(!userInfService.judgeSpace(teamUser)) return "创建失败，空间已达上限！";
        //调用创建文件夹service
        DirInf dirInf = dirInfService.selectRootDirByUserId(teamUser.getUserId()); //获得根目录
        DirInf newDeptDir = dirInfService.insertSelective(dept.getDeptName(),dirInf.getDirId(),teamUser); //创建文件夹
        FileCabinet fileCabinet = fileCabinetService.newFileCabinet(dept,teamUser,newDeptDir); //创建文件柜
        departmentService.insertSelective(fileCabinet,team);  //创建部门
        userInfService.updateSpaceWhenNewDept(teamUser); //更新团队空间
        if(userInf.getUserType() == 2) {
            UserInf newUser = userInfService.selectByPrimaryKey(userInf.getUserId());
            session.setAttribute("USER_SESSION", newUser);  //更新当前用户信息
        }
        return "创建成功！";
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
        //更新名称
        FileCabinet fileCabinet = fileCabinetService.selectByDeptId(deptId); //获得文件柜空间信息

        if(deptName != null && deptName != ""){
            System.out.println("部门名："+deptName);
            String result = dirInfService.updateDirName(fileCabinet.getDirId(), deptName);//修改文件名
            if(result.equals("文件已存在!")) return "部门已存在!";
            fileCabinetService.
                    updateByPrimaryKeySelective(fileCabinet.getFcId(),deptName,null,null,null);//更新文件柜
            departmentService.
                    updateByPrimaryKeySelective(deptId,deptName,null,null);
        }
        if (maxSpace != null ) {
            System.out.println("空间："+maxSpace);
            BigInteger maxSpaceChange = UnitChange.TranslateGBtoByte(maxSpace); //转换单位
            userInfService.
                    updateSpaceWhenEditDept(userInf,fileCabinet,maxSpaceChange);//更新用户空
            fileCabinetService.
                    updateByPrimaryKeySelective(fileCabinet.getFcId(),null,maxSpaceChange,null,null);//更新文件柜
        }
        //更新文件柜空间
        return "保存成功";
    }
}
