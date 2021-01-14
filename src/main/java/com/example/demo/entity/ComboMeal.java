package com.example.demo.entity;

import java.math.BigInteger;

public class ComboMeal {
    private Integer mealId;

    private String mealName;

    private BigInteger perSpace;

    private BigInteger teamSpace;

    private Integer deadline;

    private Integer price;

    private Integer mStatus;

    private Integer mType;

    public Integer getMealId() {
        return mealId;
    }

    public void setMealId(Integer mealId) {
        this.mealId = mealId;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName == null ? null : mealName.trim();
    }

    public BigInteger getPerSpace() {
        return perSpace;
    }

    public void setPerSpace(BigInteger perSpace) {
        this.perSpace = perSpace;
    }

    public BigInteger getTeamSpace() {
        return teamSpace;
    }

    public void setTeamSpace(BigInteger teamSpace) {
        this.teamSpace = teamSpace;
    }

    public Integer getDeadline() {
        return deadline;
    }

    public void setDeadline(Integer deadline) {
        this.deadline = deadline;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getmStatus() {
        return mStatus;
    }

    public void setmStatus(Integer mStatus) {
        this.mStatus = mStatus;
    }

    public Integer getmType() {
        return mType;
    }

    public void setmType(Integer mType) {
        this.mType = mType;
    }
}