package com.example.demo.service.impl;

import com.example.demo.entity.DeptMember;
import com.example.demo.entity.UserInf;
import com.example.demo.service.CheckPermissionsService;
import com.example.demo.service.DeptMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CheckPermissionsServiceImpl implements CheckPermissionsService {

    @Autowired
    DeptMemberService deptMemberService;

    @Override
    public Boolean checkUploadPremission(UserInf userInf) {
        //通过用户查找，用户再部门中的信息（dao没有该方法）
        DeptMember deptMember = deptMemberService.selectByUserKey(userInf.getUserId());
        //检查，成员信息中 的 p_upload 是否为1，如果则返回true,否则返回false
        if (deptMember.getpUpload() == 1) return true;
        return false;
    }

    @Override
    public Boolean checkDownloadPremission(UserInf userInf) {
        //通过用户查找，用户再部门中的信息（dao没有该方法）
        DeptMember deptMember = deptMemberService.selectByUserKey(userInf.getUserId());
        //检查，成员信息中 的 p_down 是否为1，如果是返回true,否则返回false
        if (deptMember.getpDown() == 1) return true;
        return false;
    }

    @Override
    public Boolean checkPreviewPremission(UserInf userInf) {
        //通过用户查找，用户再部门中的信息（dao没有该方法）
        DeptMember deptMember = deptMemberService.selectByUserKey(userInf.getUserId());
        //检查，成员信息中 的 p_preview 是否为1，如果是返回true,否则返回false
        if (deptMember.getpPreview() == 1) return true;
        return false;
    }
}
