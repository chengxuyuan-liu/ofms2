package com.example.demo.entity;

public class DeptInf {
    private Integer deptId;

    private String deptName;

    private Integer maxSpace;

    private Integer usedSpace;

    private Integer dirId;

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName == null ? null : deptName.trim();
    }

    public Integer getMaxSpace() {
        return maxSpace;
    }

    public void setMaxSpace(Integer maxSpace) {
        this.maxSpace = maxSpace;
    }

    public Integer getUsedSpace() {
        return usedSpace;
    }

    public void setUsedSpace(Integer usedSpace) {
        this.usedSpace = usedSpace;
    }

    public Integer getDirId() {
        return dirId;
    }

    public void setDirId(Integer dirId) {
        this.dirId = dirId;
    }
}