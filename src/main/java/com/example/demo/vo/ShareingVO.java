package com.example.demo.vo;

import com.example.demo.entity.Shareing;

public class ShareingVO extends Shareing {

    private Integer sdId;

   private String userName;

   private String deptName;

   private  String shareDescribe;

   private  String fileName;

   private Integer fileId;

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

    public String getShareDescribe() {
        return shareDescribe;
    }

    public void setShareDescribe(String shareDescribe) {
        this.shareDescribe = shareDescribe;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    @Override
    public Integer getSdId() {
        return sdId;
    }

    @Override
    public void setSdId(Integer sdId) {
        this.sdId = sdId;
    }
}
