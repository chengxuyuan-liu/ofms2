package com.example.demo.vo;

import com.example.demo.entity.DeptMember;

import java.util.Date;

public class Member extends DeptMember {
    private Integer id;

    private String userName;

    private String deptName;

    private String phone;

    private Integer usedSpace;

    private Integer maxSpace;

    private Date recent;

    private Integer pUpload;

    private Integer pDown;

    private Integer pPreview;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
