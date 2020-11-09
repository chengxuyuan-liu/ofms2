package com.example.demo.dao;


import com.example.demo.entity.DeptMember;
import com.example.demo.entity.Member;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DeptMemberDao {
    int deleteByPrimaryKey(Integer id);

    int insert(DeptMember record);

    int insertSelective(DeptMember record);

    DeptMember selectByPrimaryKey(Integer id);
    List<Member> selectListByDeptKey(Integer id);

    int updateByPrimaryKeySelective(DeptMember record);

    int updateByPrimaryKey(DeptMember record);
}