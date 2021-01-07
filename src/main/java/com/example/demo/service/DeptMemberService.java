package com.example.demo.service;

import com.example.demo.entity.DeptMember;
import com.example.demo.entity.UserInf;
import com.example.demo.vo.MemberVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeptMemberService {
    DeptMember selectByPrimaryKey(Integer id);
    List<MemberVO> selectListByDeptKey(Integer id);
    DeptMember selectByUserKey(Integer userId);
    List<MemberVO> selectToBeAssignedMemberListByUserId(Integer userId);
    List<MemberVO> selectToBeAssignedMemberByUserName(Integer userId, String userName);
    List<MemberVO> selectToBeAssignedMemberByPhone(Integer userId, String phone);
    List<MemberVO> selectListByUserId(Integer userId);
    MemberVO selectListByPhone(String phone,Integer deptId);
    List<MemberVO> selectByUserName(Integer userId,String userName);;

    int deleteByPrimaryKey(Integer id);
    int deleteByDeptId(Integer deptId);

    Boolean insertSelective(Integer deptId,String userPhone, UserInf userInf);

    Boolean updateDeptById(Integer id,Integer deptId);  //更新
    Boolean updateByPrimaryKeySelective(DeptMember record);
    Boolean dissolveMemberToBeAssigned(Integer deptId);
}
