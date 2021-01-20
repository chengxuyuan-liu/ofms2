package com.example.demo.vo;

import com.example.demo.entity.BuyRecord;

public class BuyRecordVO extends BuyRecord {
    private String email;
    private String mealName;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }
}
