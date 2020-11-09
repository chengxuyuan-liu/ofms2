package com.example.demo.service;

import com.example.demo.entity.DeptMember;
import com.example.demo.entity.Member;

import java.util.List;

public interface DeptMemberService {
    DeptMember selectByPrimaryKey(Integer id);
    List<Member> selectListByDeptKey(Integer id);
}
