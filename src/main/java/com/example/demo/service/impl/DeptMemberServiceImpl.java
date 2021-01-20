package com.example.demo.service.impl;

import com.example.demo.dao.DepartmentDao;
import com.example.demo.dao.DeptMemberDao;
import com.example.demo.dao.TeamDao;
import com.example.demo.dao.UserInfDao;
import com.example.demo.entity.*;
import com.example.demo.vo.MemberVO;
import com.example.demo.service.FileCabinetService;
import com.example.demo.service.DeptMemberService;
import com.example.demo.service.UserInfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class DeptMemberServiceImpl implements DeptMemberService {

    @Autowired
    DeptMemberDao deptMemberDao;
    @Autowired
    UserInfService userInfService;
    @Autowired
    FileCabinetService fileCabinetService;
    @Autowired
    TeamDao teamDao;
    @Autowired
    UserInfDao userInfDao;
    @Autowired
    DepartmentDao departmentDao;

    @Override
    public DeptMember selectByPrimaryKey(Integer id) {
        DeptMember deptMember = deptMemberDao.selectByPrimaryKey(id);
        return deptMember;
    }

    @Override

    public List<MemberVO> selectListByDeptKey(Integer deptId) {
        List<MemberVO> memberVOList = deptMemberDao.selectListByDeptKey(deptId);
        return memberVOList;
    }

    @Override
    public DeptMember selectByUserKey(Integer userId) {
        DeptMember deptMember = deptMemberDao.selectByUserKey(userId);
        return deptMember;
    }

    @Override
    public List<MemberVO> selectToBeAssignedMemberListByUserId(Integer userId) {
        List<MemberVO> list = deptMemberDao.selectToBeAssignedMemberListByUserId(userId);
        return list;
    }

    @Override
    public List<MemberVO> selectToBeAssignedMemberByUserName(Integer userId, String userName) {
        List<MemberVO> memberVO = deptMemberDao.selectToBeAssignedMemberByUserName(userId,userName);
        return memberVO;
    }

    @Override
    public List<MemberVO> selectToBeAssignedMemberByPhone(Integer userId, String phone) {
        List<MemberVO> memberVO = deptMemberDao.selectToBeAssignedMemberByPhone(userId,phone);
        return memberVO;
    }

    @Override
    public List<MemberVO> selectListByUserId(Integer teamId) {
        List<MemberVO> memberVO = deptMemberDao.selectListByTeamId(teamId);
        return memberVO;
    }

    @Override
    public MemberVO selectListByPhone(String phone,Integer deptId) {
        UserInf userInf = userInfDao.selectByUserPhone(phone);
        if(userInf != null){
            DeptMember deptMember = deptMemberDao.selectByUserKey(userInf.getUserId());
            if(deptMember != null && deptMember.getDeptId().equals(deptId)) {
                Department department = departmentDao.selectByPrimaryKey(deptMember.getDeptId());
                MemberVO memberVO = new MemberVO();
                memberVO.setId(deptMember.getId());
                memberVO.setUserName(userInf.getUsername());
                memberVO.setDeptName(department.getDeptName());
                memberVO.setEmail(userInf.getEmail());
                memberVO.setPhone(userInf.getUserPhone());
                memberVO.setUsedSpace(deptMember.getUsedSpace());
                memberVO.setMaxSpace(deptMember.getMaxSpace());
                memberVO.setTeamId(deptMember.getTeamId());
                memberVO.setRecent(deptMember.getRecent());
                memberVO.setpPreview(deptMember.getpPreview());
                memberVO.setpUpload(deptMember.getpUpload());
                memberVO.setpDown(deptMember.getpDown());
                memberVO.setmStatus(deptMember.getmStatus());
                memberVO.setDeptId(deptMember.getDeptId());
                return memberVO;
            }
        }
        return null;
    }

    @Override
    public List<MemberVO> selectByUserName(Integer userId, String userName) {
        List<MemberVO> memberVO = deptMemberDao.selectByUserName(userId,userName);
        return memberVO;
    }

    @Override
    public DeptMember selectByUserIdAndTeamId(Integer userId, Integer teamId) {
        DeptMember deptMember = deptMemberDao.selectByUserIdAndTeamId(userId,teamId);
        return deptMember;
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        int result = deptMemberDao.deleteByPrimaryKey(id);
        return result;
    }
    public int deleteByDeptId(Integer deptId){
        int result = deptMemberDao.deleteByDeptId(deptId);
        return  result;
    };

    @Override
    public Boolean insertSelective(Integer deptId,String userPhone,UserInf userInf) {
        //通过userPhone获取用户对象，通过用户对象获得userId
        UserInf newMember = userInfService.selectByUserPhone(userPhone);   //通过手机号查询用户

        if(userInf != null){

            Team team = teamDao.selectByUserId(userInf.getUserId());    //通过用户id查询团队
            //封装成员信息成DeptMember对象
            DeptMember deptMember = new DeptMember();
            deptMember.setDeptId(deptId);           //部门id
            deptMember.setUserId(newMember.getUserId());    //用户id
            deptMember.setTeamId(team.getTeamId()); //团队id
            Integer result = deptMemberDao.insertSelective(deptMember);
            if(result != 0) return true;
        }

        return false;

    }

    @Override
    public Boolean updateDeptById(Integer id,Integer deptId) {

        int result = deptMemberDao.updateDeptById(id,deptId);
        System.out.println("id:"+id+"deptId:"+deptId);
        if(result != 0) return true;
        return false;
    }

    @Override
    public Boolean updateByPrimaryKeySelective(DeptMember record) {
        if(deptMemberDao.updateByPrimaryKeySelective(record) == 0) return false;
        return true;
    }

    /*
    * 解散部门时，解散成员：把部门成员状态改成未分配
    * */
    @Override
    public Boolean dissolveMemberToBeAssigned(Integer deptId) {
        if(deptMemberDao.updateMemberByDeptId(deptId) == 0) return false;
        return true;
    }

}
