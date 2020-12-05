package com.example.demo.entity;

import java.math.BigInteger;
import java.util.Date;

public class DeptMember {
    private Integer id;

    private Integer userId;

    private Integer deptId;

    private Integer mStatus;

    private BigInteger usedSpace;

    private BigInteger maxSpace;

    private Date recent;

    private Integer pUpload;

    private Integer pDown;

    private Integer pPreview;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public BigInteger getUsedSpace() {
        return usedSpace;
    }

    public void setUsedSpace(BigInteger usedSpace) {
        this.usedSpace = usedSpace;
    }

    public BigInteger getMaxSpace() {
        return maxSpace;
    }

    public void setMaxSpace(BigInteger maxSpace) {
        this.maxSpace = maxSpace;
    }

    public Date getRecent() {
        return recent;
    }

    public void setRecent(Date recent) {
        this.recent = recent;
    }

    public Integer getpUpload() {
        return pUpload;
    }

    public void setpUpload(Integer pUpload) {
        this.pUpload = pUpload;
    }

    public Integer getpDown() {
        return pDown;
    }

    public void setpDown(Integer pDown) {
        this.pDown = pDown;
    }

    public Integer getpPreview() {
        return pPreview;
    }

    public void setpPreview(Integer pPreview) {
        this.pPreview = pPreview;
    }

    public Integer getmStatus() { return mStatus; }

    public void setmStatus(Integer mStatus) { this.mStatus = mStatus; }
}