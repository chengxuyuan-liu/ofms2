package com.example.demo.entity;

import java.util.Date;

public class CollectionDetail {
    private Integer cdId;

    private String cdDescribe;

    private Date deadline;

    private Integer allNum;

    private Integer uploadNum;

    private Integer cdStatus;

    private Integer userId;

    private Integer dirId;

    private String savePath;

    public Integer getCdId() {
        return cdId;
    }

    public void setCdId(Integer cdId) {
        this.cdId = cdId;
    }

    public String getCdDescribe() {
        return cdDescribe;
    }

    public void setCdDescribe(String cdDescribe) {
        this.cdDescribe = cdDescribe == null ? null : cdDescribe.trim();
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Integer getAllNum() {
        return allNum;
    }

    public void setAllNum(Integer allNum) {
        this.allNum = allNum;
    }

    public Integer getUploadNum() {
        return uploadNum;
    }

    public void setUploadNum(Integer uploadNum) {
        this.uploadNum = uploadNum;
    }

    public Integer getCdStatus() {
        return cdStatus;
    }

    public void setCdStatus(Integer cdStatus) {
        this.cdStatus = cdStatus;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getDirId() {
        return dirId;
    }

    public void setDirId(Integer dirId) {
        this.dirId = dirId;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }
}