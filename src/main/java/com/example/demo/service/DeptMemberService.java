package com.example.demo.service;

import com.example.demo.entity.DeptMember;
import com.example.demo.entity.Member;

import java.util.List;

public interface DeptMemberService {
    DeptMember selectByPrimaryKey(Integer id);
    List<Member> selectListByDeptKey(Integer id);
    DeptMember selectByUserKey(Integer userId);
    List<Member> selectToBeAssignedMemberListByDeptKey(Integer userId);
    List<Member> selectToBeAssignedMemberByUserName(Integer userId,String userName);
    List<Member> selectToBeAssignedMemberByPhone(Integer userId,String phone);

    int deleteByPrimaryKey(Integer id);

    Boolean insertSelective(Integer deptId,String userPhone);

    Boolean updateDeptById(Integer id,Integer deptId);  //更新
    Boolean updateByPrimaryKeySelective(DeptMember record);
}
