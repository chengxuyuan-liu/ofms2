package com.example.demo.vo;

import com.example.demo.entity.Collect;

import java.util.Date;

public class CollectVO extends Collect {

    private String userName;

    private String deptName;

    private  String collectDescribe;

    private Date deadline;

    private Integer describeStatus;

    private Integer uploadStatus;

    private String uploadFileName;

    private Integer dirId;


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

    public String getCollectDescribe() {
        return collectDescribe;
    }

    public void setCollectDescribe(String collectDescribe) {
        this.collectDescribe = collectDescribe;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Integer getDescribeStatus() {
        return describeStatus;
    }

    public void setDescribeStatus(Integer describeStatus) {
        this.describeStatus = describeStatus;
    }

    public Integer getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(Integer uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public String getUploadFileName() {
        return uploadFileName;
    }

    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }

    public Integer getDirId() {
        return dirId;
    }

    public void setDirId(Integer dirId) {
        this.dirId = dirId;
    }
}
