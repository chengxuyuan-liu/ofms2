package com.example.demo.entity;

import java.math.BigInteger;
import java.util.Date;

public class UserInf {
    private Integer userId;

    private String userPhone;

    private String username;

    private String password;

    private String email;

    private Date registerTime;

    private Integer status;

    private Integer userType;

    private BigInteger maxSpace;

    private BigInteger usedSpace;


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public BigInteger getMaxSpace() {
        return maxSpace;
    }

    public void setMaxSpace(BigInteger maxSpace) {
        this.maxSpace = maxSpace;
    }

    public BigInteger getUsedSpace() {
        return usedSpace;
    }

    public void setUsedSpace(BigInteger usedSpace) {
        this.usedSpace = usedSpace;
    }

    @Override
    public String toString() {
        return "UserInf{" +
                "userId=" + userId +
                ", userPhone='" + userPhone + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", registerTime=" + registerTime +
                ", status=" + status +
                ", userType=" + userType +
                ", maxSpace=" + maxSpace +
                ", usedSpace=" + usedSpace +
                '}';
    }
}