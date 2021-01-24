package com.example.demo.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.UserInfDao;
import com.example.demo.entity.*;
import com.example.demo.service.*;
import com.example.demo.vo.MemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RequestMapping("/shareDetail")
@Controller
public class ShareDetailController {

    @Autowired
    ShareDetailService shareDetailService;
    @Autowired
    DirInfService dirInfService;
    @Autowired
    FileInfServive fileInfServive;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    DeptMemberService deptMemberService;
    @Autowired
    TeamService teamService;
    @Autowired
    UserInfDao userInfDao;


    /*
    * 显示我的共享
    * */
    @RequestMapping("/all")
    public String all(HttpSession session, Map<String,Object> map){
        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION"); //当前用户
        //获得根文件夹
        DirInf rootDir=dirInfService.selectRootDirByUserId(userInf.getUserId());
        DirInf myDir= dirInfService.selectDirByDirName("我的文件",rootDir.getDirId());
        Integer dirId = myDir.getDirId();
        //文件目录
        List<DirInf> originAccessPath = null;     //访问路径
        List<DirInf> accessPath = new ArrayList<>();     //访问路径
        //没排序的导航路径
        originAccessPath = dirInfService.selectParentDirByDirId(dirId);
        //导航路径排序
        Integer item = dirId;
        for (int i = 0; i < originAccessPath.size(); i++) {
            for (DirInf inf : originAccessPath) {
                if(inf.getDirId().equals(item)){
                    accessPath.add(0,inf);
                    item = inf.getParentDir();
                }
            }
        }
        System.out.println(accessPath.get(0).getDirName());
        map.put("accessPath",accessPath);
        map.put("dirId",dirId);
        //部门目录
        //查询我发起的共享，并返回
        List<ShareDetail> shareDetails = shareDetailService.selectAll(userInf.getUserId());
        map.put("shareDetails",shareDetails);
        return "my_distribute";
    }

    /*
    * 获取发起共享窗口中需要的所有文件数据
    * */
    @ResponseBody
    @RequestMapping("/getDirListAadFileList")
    public String getDirListAadFileList(Map<String,Object> map,Integer dirId,Integer parentId){
        JSONArray tree = new JSONArray();   //json数组
        //所有文件夹
        List<DirInf> list = dirInfService.selectChildrenDirByDirId(parentId);  //用户根文件夹下子文件夹
        List<DirInf> list2 = dirInfService.selectChildrenDirByDirId(dirId);  //所有子文件夹
        //所有文件
        List<FileInf> fileInfs = new ArrayList<>();
        for (DirInf dirInf : list) {
            List<FileInf> fileInfList = fileInfServive.selectFileListByFolderId(dirInf.getDirId());
            fileInfs.addAll(fileInfList);
        }
        JSONObject obj;
        for(DirInf resOwner : list){
            obj = new JSONObject();
            obj.put("id", resOwner.getDirId());
            obj.put("isParent", true);
            obj.put("pId", resOwner.getParentDir());
            obj.put("name", resOwner.getDirName());
            tree.add(obj);
        }
        for (FileInf fileInf : fileInfs) {
            obj = new JSONObject();
            obj.put("id", fileInf.getFileId());
            obj.put("pId", fileInf.getDirId());
            obj.put("name", fileInf.getFileName());
            tree.add(obj);
        }
        map.put("success", new Boolean(true));
        map.put("data", tree);
        return tree.toJSONString();
    }

    /*
    * 获取发起共享窗口中需要的所有部门和成员数据
    * */
    @ResponseBody
    @RequestMapping("/getAllDeptMember")
    public String getAllDeptMember(Map<String,Object> map,HttpSession session){
        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION"); //当前用户
        //所有部门成员
        List<MemberVO> allMemberVOS = new ArrayList<>();
        //构建数据
        JSONArray tree = new JSONArray();   //最后返回的数据
        JSONObject dept; //部门
        JSONArray childrenTree = null; //成员数组

        //判断用户类型,获得所有部门信息
        List<Department> departments;
        DeptMember deptMember = deptMemberService.selectByUserKey(userInf.getUserId()); //获取成员信息
        Team team = teamService.selectByPrimaryKey(deptMember.getTeamId()); //获取团队信息
        departments = departmentService.selectByTeamId(team.getTeamId());   //所有部门
        if(userInf.getUserType().equals(1)) {
            DeptMember  leadMember = deptMemberService.selectByUserKey(team.getUserId());
            UserInf leader = userInfDao.selectByPrimaryKey(team.getUserId());
            JSONObject leaderMember = new JSONObject();
            leaderMember.put("name",leader.getUsername());
            leaderMember.put("id",leadMember.getId());
            childrenTree = new JSONArray();; //成员数组
            childrenTree.add(leaderMember);

            JSONObject own  = new JSONObject();
            own.put("name","团队负责人");
            own.put("id",null);
            own.put("children",childrenTree);

            tree.add(own);
        }





        for (Department department : departments) {
            JSONObject member=null; //成员
            childrenTree = new JSONArray();//部门所有成员
            List<MemberVO> memberVO1 = deptMemberService.selectListByDeptKey(department.getDeptId()); //查询部门所有成员
                for (MemberVO memberVO : memberVO1) {
                    if(memberVO.getUserId().equals(userInf.getUserId())) continue;
                    member = new JSONObject();
                    member.put("name", memberVO.getUserName());
                    member.put("id", memberVO.getId());
                    member.put("children", memberVO.getId());
                    childrenTree.add(member);
                }

            dept = new JSONObject();
            dept.put("name",department.getDeptName());
            if(member != null) {
                dept.put("children", childrenTree);
            }
            dept.put("id",null);
            tree.add(dept);
        }
        System.out.println(tree.toJSONString());
        return tree.toJSONString();
    }




    //搜索
    @ResponseBody
    @RequestMapping("/search")
    public void search(String shareName,Map<String,Object> map){
        //根据共享关键字进行模糊查询

    }

    /*
    * 添加共享
    * */
    @ResponseBody
    @RequestMapping("/add")
    public String add(HttpSession session,Map<String,Object> map,
                    Integer fileId,String memberId,Integer memberNum,String describe){
        //添加共享
        //参数：描述、文件id、选中成员人数、成员id
        System.out.println(memberId);
        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");
        return shareDetailService.add(fileId,memberId,memberNum,describe,userInf.getUserId());

    }

    //删除共享
    @ResponseBody
    @RequestMapping("/delete")
    public String delete(String[] sdId){
        //
        return shareDetailService.delete(sdId);
    }
}
