package com.example.demo.dao;


import com.example.demo.entity.DeptMember;
import com.example.demo.vo.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DeptMemberDao {

    /*
    删除
    */
    int deleteByPrimaryKey(Integer id);
    int deleteByDeptId(Integer deptId);

    /*
    插入
    */
    int insert(DeptMember record);
    int insertSelective(DeptMember record);

    /*
    查询
    */
    DeptMember selectByPrimaryKey(Integer id);
    List<Member> selectListByDeptKey(Integer id);
    DeptMember selectByUserKey(Integer userId);
    List<Member> selectToBeAssignedMemberListByUserId(Integer userId);
    List<Member> selectToBeAssignedMemberByUserName(@Param("userId")Integer userId,@Param("userName")String userName);
    List<Member> selectToBeAssignedMemberByPhone(@Param("userId")Integer userId,@Param("phone")String phone);

    /*
    更改
    */
    int updateByPrimaryKeySelective(DeptMember record);
    int updateByPrimaryKey(DeptMember record);
    int updateDeptById(Integer id ,Integer deptId);         //根据部门id修改部门成员,修改status
}