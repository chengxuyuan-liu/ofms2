package com.example.demo.vo;

import com.example.demo.entity.DeptMember;

import java.math.BigInteger;
import java.util.Date;

public class Member extends DeptMember {
    private Integer id;

    private String userName;

    private String deptName;

    private String phone;

    private String email;

    private BigInteger usedSpace;

    private BigInteger maxSpace;

    private Date recent;

    private Integer pUpload;

    private Integer pDown;

    private Integer pPreview;

    public Member() {

    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Member(Integer id, String userName, String deptName, String phone, String email, BigInteger usedSpace, BigInteger maxSpace, Date recent, Integer pUpload, Integer pDown, Integer pPreview) {
        this.id = id;
        this.userName = userName;
        this.deptName = deptName;
        this.phone = phone;
        this.email = email;
        this.usedSpace = usedSpace;
        this.maxSpace = maxSpace;
        this.recent = recent;
        this.pUpload = pUpload;
        this.pDown = pDown;
        this.pPreview = pPreview;
    }
}
