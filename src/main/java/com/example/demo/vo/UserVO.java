package com.example.demo.vo;

import com.example.demo.entity.UserInf;

import java.math.BigInteger;

public class UserVO extends UserInf {
    //个人空间
    private BigInteger perSpace;

    public BigInteger getPerSpace() {
        return perSpace;
    }

    public void setPerSpace(BigInteger perSpace) {
        this.perSpace = perSpace;
    }
}
