package com.example.demo.entity;

public class DirInf {
    private Integer dirId;

    private String dirName;

    private Integer parentDir;

    private Integer userId;

    private String dirPath;

    public Integer getDirId() {
        return dirId;
    }

    public void setDirId(Integer dirId) {
        this.dirId = dirId;
    }

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName == null ? null : dirName.trim();
    }

    public Integer getParentDir() {
        return parentDir;
    }

    public void setParentDir(Integer parentDir) {
        this.parentDir = parentDir;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getDirPath() {
        return dirPath;
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath == null ? null : dirPath.trim();
    }
}