package com.example.demo.service.impl;

import com.example.demo.dao.DeptMemberDao;
import com.example.demo.entity.DeptMember;
import com.example.demo.entity.Member;
import com.example.demo.service.DeptMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeptMemberServiceImpl implements DeptMemberService {

    @Autowired
    DeptMemberDao deptMemberDao;

    @Override
    public DeptMember selectByPrimaryKey(Integer id) {
        DeptMember deptMember = deptMemberDao.selectByPrimaryKey(id);
        return deptMember;
    }

    @Override
    public List<Member> selectListByDeptKey(Integer deptId) {
        List<Member> memberList = deptMemberDao.selectListByDeptKey(deptId);
        return memberList;
    }
}
