package com.example.demo.controller;


import com.example.demo.entity.*;
import com.example.demo.service.*;
import com.example.demo.vo.Member;
import com.example.demo.vo.PermissionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class ViewController {
    @Autowired
    FileInfServive fileInfServive;
    @Autowired
    DirInfService dirInfService;
    @Autowired
    FileCabinetService fileCabinetService;
    @Autowired
    DeptMemberService deptMemberService;
    @Autowired
    UserInfService userInfService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    TeamService teamService;
    @Autowired
    PermissionService permissionService;
    @Autowired
    UserLogService userLogService;

    /**
     * 跳转到菜单页面
     **/
    @RequestMapping("/menu")
    public String toMenuView(Map<String,Object> map, HttpSession session){
        //当前用户
        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");
        //获得用户或用户所在部门的文件柜
        List<FileCabinet> fileCabinetList = fileCabinetService.selectByUserId(userInf.getUserId());
        //如果是团队账号,获取部门列表
        if(userInf.getUserType() == 2){
            List<Department> departmentList = departmentService.selectDeptListByUserId(userInf.getUserId());
            map.put("deptList", departmentList);
        }

        //如果是个人账号--》团队、部门
        if(userInf.getUserType() == 1){
            DeptMember deptMember=deptMemberService.selectByUserKey(userInf.getUserId()); //用户的成员信息
            Team team = teamService.selectByPrimaryKey(deptMember.getTeamId());  //用户所属团队
            Department department = departmentService.selectByPrimaryKey(deptMember.getDeptId()); //用户所属部门
            map.put("team",team);
            map.put("department", department);

            Permission permission = permissionService.selectByMemberId(deptMember.getId());
            if(permission != null){
                if(permission.getpAddDept() == 1 || permission.getpEditDept() ==1){
                    List<Department> departmentList = departmentService.selectByTeamId(team.getTeamId());
                    map.put("deptList", departmentList);        //获得团队所有部门

                }else{
                    map.put("deptList", departmentService.selectByPrimaryKey(deptMember.getDeptId()));
                }
            }
            //获取权限信息
           map.put("permission", permission);
        }
        map.put("fileCabinetList", fileCabinetList);

        return "menu";
    }


    /**
     * 文件管理页面主要数据
    **/
    @RequestMapping("/main")
    public String mainView(Integer dirId,Map<String,Object> map,HttpSession session){

        //调用业务
        List<DirInf> dirInfList;          //文件夹List
        List<FileInf> fileInfList;         //文件List
        List<DirInf> accessPath = null;     //访问路径
        DeptMember deptMember = null;

        FileCabinet fileCabinet;//当前文件的文件柜
        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");

        //如果dirId为空
        if(dirId != null)
        {
            //导航路径
            accessPath = dirInfService.selectParentDirByDirId(dirId);
            DirInf dirInf = dirInfService.selectByPrimaryKey(dirId);
            if(userInf.getUserId() == dirInf.getUserId() ) {
                fileCabinet = fileCabinetService.selectByDirId(accessPath.get(0).getDirId());
                map.put("fileCabinet", fileCabinet);
            }else{
                deptMember = deptMemberService.selectByUserKey(userInf.getUserId());
                map.put("deptMember", deptMember);
            }

            //获得文件id为dirId文件夹下的文件夹、文件
            dirInfList = dirInfService.selectDirListByParentDirId(dirId);
            fileInfList = fileInfServive.selectFileListByFolderId(dirId);

        }
        else
        {
            //获得根文件夹
            DirInf rootDir= dirInfService.selectRootChildrenDirByUserId(userInf.getUserId(),"我的文件");
            dirId = rootDir.getDirId();
            fileCabinet  = fileCabinetService.selectByDirId(rootDir.getDirId());
            map.put("fileCabinet", fileCabinet);
            //获得根文件夹下的文件夹
            dirInfList = dirInfService.selectDirListByParentDirId(rootDir.getDirId());
            //获得根文件夹下的文件
            fileInfList = fileInfServive.selectFileListByFolderId(rootDir.getDirId());
            //导航路径
            accessPath = dirInfService.selectParentDirByDirId(rootDir.getDirId());

        }
        //返回


        map.put("dirId",dirId);
        map.put("fileList",fileInfList);
        map.put("dirList",dirInfList);
        map.put("accessPath",accessPath);
        return "file_manage";

    }




    /**
     * 查询结果
     **/
    @RequestMapping("/query")
    public String queryResult(String queryName,Map<String,Object> map,HttpSession session) {

        //获得当前用户
        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");

        //文件柜
        //FileCabinet fileCabinet = fileCabinetService.selectByDirId();
        //文件
        List<FileInf> fileInfList = fileInfServive.selectByFileName(queryName,userInf.getUserId());
        //文件夹
        List<DirInf> dirInfList = dirInfService.selectByDirName(queryName,userInf.getUserId());

        DirInf dirInf = new DirInf();
        dirInf.setDirName("《=\""+queryName+"\""+"搜索结果");
        List<DirInf> accessPath = new ArrayList<>();
        accessPath.add(dirInf);

        //返回map
        map.put("fileList",fileInfList);
        map.put("dirList",dirInfList);
        map.put("accessPath",accessPath);

        return "file_manage";
    }

    /**
     * 成员管理页面
     **/
    @RequestMapping("/deptMemberView")
    public String deptMember(Integer deptId , Map<String,Object> map , HttpSession session) {

        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");
        if(userInf.getUserType() == 1) {
            DeptMember deptMember = deptMemberService.selectByUserKey(userInf.getUserId());
            Permission permission = permissionService.selectByMemberId(deptMember.getId());
            map.put("permission", permission);
        }
        //通过部门Id获得部门成员List
        List<Member> memberList = deptMemberService.selectListByDeptKey(deptId);
        //部门文件柜
        FileCabinet fileCabinet = fileCabinetService.selectByDeptId(deptId);

        //返回
        map.put("fileCabinet",fileCabinet);
        map.put("memberList",memberList);
        map.put("deptId",deptId);

        return "dept_member";
    }


    /**
     * 成员管理搜索
     **/
    @RequestMapping("/searchMember")
    public String searchMember(String userPhone , Map<String,Object> map , HttpSession session) {

        Member member = deptMemberService.selectListByPhone(userPhone);
        List<Member> memberList = new ArrayList<>();
        memberList.add(member);

        //部门文件柜
        FileCabinet fileCabinet = fileCabinetService.selectByDeptId(member.getDeptId());

        //返回
        map.put("fileCabinet",fileCabinet);

        map.put("memberList",memberList);
        //返回
        return "dept_member";
    }


    /**
     * 成员管理页面-->待分配
     **/
    @RequestMapping("/toBeAssigned")
    public String toBeAssigned(Map<String,Object> map ,HttpSession session) {
        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");
        Team team = teamService.selectByUserId(userInf);

        //如果是个人账号
        if(userInf.getUserType() == 1){
            DeptMember deptMember = deptMemberService.selectByUserKey(userInf.getUserId());//通过成员信息获取团队id
            team = teamService.selectByPrimaryKey(deptMember.getTeamId()); //通过团队信息获取用户id
        }

        List<Member> memberList = deptMemberService.selectToBeAssignedMemberListByUserId(team.getUserId());  //获取所有待分配成员

        List<Department> departmentList = departmentService.selectDeptListByUserId(team.getUserId());    //获取所有部门，用于编辑时是使用



        List<Department> department = departmentService.selectByTeamId(team.getTeamId());

        List<FileCabinet> fileCabinetList = new ArrayList<>();
        for (Department department1 : department) {
            fileCabinetList.add( fileCabinetService.selectByDeptId(department1.getDeptId()));//获取所有文件柜，用于编辑时是使用
        }


        map.put("memberList",memberList);
        map.put("deptInfList", departmentList);
        map.put("fileCabinetList", fileCabinetList);
        return "to_be_assigned";
    }

        /*
    待分配-搜索
    */
    @RequestMapping("/toBeAssignedSearch")
    public String toBeAssignedSearch(Integer searchType,String userName,String phone,Map<String,Object> map,HttpSession session) {
        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");
        List<Member> memberList = new ArrayList<>();
        if(searchType==1){
            System.out.println("用户名："+userName);
            memberList = deptMemberService.selectToBeAssignedMemberByUserName(userInf.getUserId(),userName);
        }else{
            System.out.println("手机号码："+phone);
            memberList = deptMemberService.selectToBeAssignedMemberByPhone(userInf.getUserId(),phone);
        }
        map.put("memberList",memberList);
        return "to_be_assigned";
    }



    /**
     * 管理用户界面
     **/
    @RequestMapping("/toAdminstrationUser")
    public String toAdminstrationUser(PageRequest pageRequest,Map<String,Object> map ,HttpSession session) {
        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");
//        List<UserInf> userInfList = userInfService.selectAll();
//        map.put("userInfList",userInfList);

        PageResult pageResult = userInfService.findPage(pageRequest);

        map.put("pageResult",pageResult);
        return "adminstration_user";
    }


    /**
     * 权限管理界面
     **/
    @RequestMapping("/permission")
    public String toPermission(HttpSession session,Map<String,Object> map ) {

        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");
        List<PermissionVO> permissionVOS = permissionService.selectByUserId(userInf.getUserId());
        List<Department> departmentList = departmentService.selectDeptListByUserId(userInf.getUserId());

        map.put("department",departmentList);
        map.put("permission",permissionVOS);
        return "permission";
    }


    /**
     * 权限管理界面---搜索
     **/
    @RequestMapping("/permissionSearch")
    public String permissionSearch(String phone,HttpSession session,Map<String,Object> map ) {

        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");
        List<PermissionVO> permissionVOS = permissionService.selectByPhone(phone);
        List<Department> departmentList = departmentService.selectDeptListByUserId(userInf.getUserId());

        map.put("department",departmentList);
        map.put("permission",permissionVOS);
        return "permission";
    }









    /**
     * 管理员用户搜索
     **/
    @RequestMapping("/userSearch")
    public String userSearch(Integer searchType,String userName,String phone,Map<String,Object> map,HttpSession session) {

        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");
        List<UserInf> userInfList = new ArrayList<>();
        if(searchType==1){
            System.out.println("用户名："+userName);
            userInfList = userInfService.selectListByUserName(userName);
        }else{
            System.out.println("手机号码："+phone);
            UserInf userInf1 = userInfService.selectByUserPhone(phone);
            userInfList.add(userInf1);
        }
        map.put("userInfList",userInfList);
        return "adminstration_user";
    }


    @RequestMapping("/toAdminstrationLog")
    public Object  toAdminstrationLog() {
        return "redirect:/userLog?pageNum=1&pageSize=10";
    }



    /**
     * 查看日志
     **/
    @RequestMapping("/userLog")
    public Object  userLog(PageRequest pageQuery,Map<String,Object> map) {
        //获得所有日志
        PageResult pageResult = userLogService.findPage(pageQuery);
        List<UserLog> userLogs = (List<UserLog>) pageResult.getContent();
        map.put("pageRsult",pageResult);
        return "adminstration_log";
    }


}
