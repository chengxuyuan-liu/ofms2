package com.example.demo.service.impl;

import com.example.demo.dao.*;
import com.example.demo.entity.*;
import com.example.demo.service.PermissionService;
import com.example.demo.service.TeamService;
import com.example.demo.service.UserInfService;
import com.example.demo.vo.Member;
import com.example.demo.vo.PermissionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    PermissionDao permissionDao;
    @Autowired
    DeptMemberDao deptMemberDao;
    @Autowired
    TeamDao teamDao;
    @Autowired
    UserInfDao userInfDao;
    @Autowired
    DepartmentDao departmentDao;


    //删除
    @Override
    public int deleteByPrimaryKey(Integer psiId) {
        int reulst = permissionDao.deleteByPrimaryKey(psiId);
        return reulst;
    }

    //插入
    @Override
    public int insert(Permission record) {
        int reulst = permissionDao.insert(record);
        return reulst;
    }

    @Override
    public int insertSelective(Permission record) {
        int reulst = permissionDao.insertSelective(record);
        return reulst;
    }


    //查询
    @Override
    public Permission selectByPrimaryKey(Integer psiId) {
        Permission permission =permissionDao.selectByPrimaryKey(psiId);
        return permission;
    }

    @Override
    public List<PermissionVO> selectByUserId(Integer userId) {

        //
        Team team = teamDao.selectByUserId(userId);
        //查询成员表
        List<Member> members = deptMemberDao.selectListByTeamId(team.getTeamId());
        //封装
        List<PermissionVO> permissions= new ArrayList<PermissionVO>();
        for (Member member : members) {
            PermissionVO permissionVO = new PermissionVO();
            Permission permission1 = permissionDao.selectByMemberId(member.getId());
            if(permission1 == null) continue;
            permissionVO.setPsiId(permission1.getPsiId());
            permissionVO.setRole(permission1.getRole());
            permissionVO.setUserName(member.getUserName());
            permissionVO.setEmail(member.getEmail());
            permissionVO.setPhone(member.getPhone());
            permissionVO.setDeptName(member.getDeptName());
            permissionVO.setRecent(member.getRecent());
            permissions.add(permissionVO);
        }
        return permissions;
    }

    @Override
    public List<PermissionVO> selectByPhone(String phone) {

        //姓名、邮箱、手机号
        UserInf userInf = userInfDao.selectByUserPhone(phone);
        if(userInf == null) return null;  //没有这个账户
        DeptMember deptMember = deptMemberDao.selectByUserKey(userInf.getUserId());
        if(deptMember == null) return null; //没有没有这个成员
        Permission permission = permissionDao.selectByMemberId(deptMember.getId());
        if(permission == null) return null;   //没有没有这个角色

        //封装
        List<PermissionVO> permissions= new ArrayList<PermissionVO>();

        PermissionVO permissionVO = new PermissionVO();

        permissionVO.setPsiId(permission.getPsiId());
        permissionVO.setRole(permission.getRole());
        permissionVO.setUserName(userInf.getUsername());
        permissionVO.setEmail(userInf.getEmail());
        permissionVO.setPhone(userInf.getUserPhone());
        permissionVO.setRecent(deptMember.getRecent());

        Department department = departmentDao.selectByPrimaryKey(deptMember.getDeptId());
        permissionVO.setDeptName(department.getDeptName());
        permissions.add(permissionVO);

        return permissions;
    }


    //查询
    @Override
    public PermissionVO selectByPsiId(Integer psiId) {
        System.out.println(psiId);
        Permission permission =permissionDao.selectByPrimaryKey(psiId);
        DeptMember deptMember = deptMemberDao.selectByPrimaryKey(permission.getMemberId());
        UserInf userInf = userInfDao.selectByPrimaryKey(deptMember.getUserId());
        PermissionVO permissionVO = new PermissionVO();

        permissionVO.setPsiId(permission.getPsiId());
        permissionVO.setRole(permission.getRole());
        permissionVO.setUserName(userInf.getUsername());
        permissionVO.setpAddDept(permission.getpAddDept());
        permissionVO.setpAddMember(permission.getpAddMember());
        permissionVO.setpDelMember(permission.getpDelMember());
        permissionVO.setpEditDept(permission.getpEditDept());
        permissionVO.setpEditMember(permission.getpEditMember());
        return permissionVO;
    }

    @Override
    public Permission selectByMemberId(Integer memberId) {
        Permission permission = permissionDao.selectByMemberId(memberId);

        return permission;
    }


    //更新
    @Override
    public int updateByPrimaryKeySelective(Permission record) {
        int reulst = permissionDao.updateByPrimaryKeySelective(record);
        return reulst;
    }

    @Override
    public int updateByPrimaryKey(Permission record) {
        int reulst = permissionDao.updateByPrimaryKey(record);
        return reulst;
    }
}
