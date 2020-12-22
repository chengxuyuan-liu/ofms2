package com.example.demo.service;

import com.example.demo.entity.Department;
import com.example.demo.entity.DeptMember;
import com.example.demo.entity.UserInf;
import com.example.demo.vo.Member;

import java.util.List;

public interface DeptMemberService {
    DeptMember selectByPrimaryKey(Integer id);
    List<Member> selectListByDeptKey(Integer id);
    DeptMember selectByUserKey(Integer userId);
    List<Member> selectToBeAssignedMemberListByUserId(Integer userId);
    List<Member> selectToBeAssignedMemberByUserName(Integer userId,String userName);
    List<Member> selectToBeAssignedMemberByPhone(Integer userId,String phone);
    List<Member> selectListByUserId(Integer userId);
    Member selectListByPhone(String phone);

    int deleteByPrimaryKey(Integer id);
    int deleteByDeptId(Integer deptId);

    Boolean insertSelective(Integer deptId,String userPhone, UserInf userInf);

    Boolean updateDeptById(Integer id,Integer deptId);  //更新
    Boolean updateByPrimaryKeySelective(DeptMember record);
    Boolean dissolveMemberToBeAssigned(Integer deptId);
}
