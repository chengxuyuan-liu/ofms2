package com.example.demo.service.impl;

import com.example.demo.dao.DeptMemberDao;
import com.example.demo.entity.FileCabinet;
import com.example.demo.entity.DeptMember;
import com.example.demo.vo.Member;
import com.example.demo.entity.UserInf;
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

    @Override
    public DeptMember selectByUserKey(Integer userId) {
        DeptMember deptMember = deptMemberDao.selectByUserKey(userId);
        return deptMember;
    }

    @Override
    public List<Member> selectToBeAssignedMemberListByUserId(Integer userId) {
        List<Member> list = deptMemberDao.selectToBeAssignedMemberListByUserId(userId);
        return list;
    }

    @Override
    public List<Member> selectToBeAssignedMemberByUserName(Integer userId,String userName) {
        List<Member> member = deptMemberDao.selectToBeAssignedMemberByUserName(userId,userName);
        return member;
    }

    @Override
    public List<Member> selectToBeAssignedMemberByPhone(Integer userId,String phone) {
        List<Member> member = deptMemberDao.selectToBeAssignedMemberByPhone(userId,phone);
        return member;
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
    public Boolean insertSelective(Integer deptId,String userPhone) {
        //通过userPhone获取用户对象，通过用户对象获得userId
        UserInf userInf = userInfService.selectByUserPhone(userPhone);

        if(userInf != null){
            //通过deptId获得部门对象，通过部门对象获得maxSpace
            FileCabinet fileCabinet = fileCabinetService.selectByPrimaryKey(deptId);
            //封装成员信息成DeptMember对象
            DeptMember deptMember = new DeptMember();
            deptMember.setDeptId(deptId);
            deptMember.setUserId(userInf.getUserId());
            deptMember.setMaxSpace(new BigInteger(fileCabinet.getMaxSpace().toString()));  //默认为部门最大值

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


}
