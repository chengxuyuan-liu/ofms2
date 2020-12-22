package com.example.demo.vo;

import com.example.demo.entity.Permission;

import java.util.Date;

public class PermissionVO extends Permission {
    private String userName;

    private String deptName;

    private String phone;

    private String email;

    private Date recent;

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

    public Date getRecent() {
        return recent;
    }

    public void setRecent(Date recent) {
        this.recent = recent;
    }
}
