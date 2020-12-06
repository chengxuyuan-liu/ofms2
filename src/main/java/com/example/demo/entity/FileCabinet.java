package com.example.demo.entity;

import java.math.BigInteger;

public class FileCabinet {
    private Integer fcId;

    private String fcName;

    private BigInteger maxSpace;

    private BigInteger usedSpace;
    
    private Integer dirId;

    public Integer getFcId() {
        return fcId;
    }

    public void setFcId(Integer fcId) {
        this.fcId = fcId;
    }

    public String getFcName() {
        return fcName;
    }

    public void setFcName(String fcName) {
        this.fcName = fcName == null ? null : fcName.trim();
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

    public Integer getDirId() {
        return dirId;
    }

    public void setDirId(Integer dirId) {
        this.dirId = dirId;
    }
    
}