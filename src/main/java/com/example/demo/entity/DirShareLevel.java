package com.example.demo.entity;

public class DirShareLevel {
    private Integer dslId;

    private Integer sLevel;

    private String levelDescribe;

    private Integer dirId;

    public Integer getDslId() {
        return dslId;
    }

    public void setDslId(Integer dslId) {
        this.dslId = dslId;
    }

    public Integer getsLevel() {
        return sLevel;
    }

    public void setsLevel(Integer sLevel) {
        this.sLevel = sLevel;
    }

    public String getLevelDescribe() {
        return levelDescribe;
    }

    public void setLevelDescribe(String levelDescribe) {
        this.levelDescribe = levelDescribe == null ? null : levelDescribe.trim();
    }

    public Integer getDirId() {
        return dirId;
    }

    public void setDirId(Integer dirId) {
        this.dirId = dirId;
    }
}