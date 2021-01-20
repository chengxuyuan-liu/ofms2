package com.example.demo.vo;

import com.example.demo.entity.FileInf;

public class FileVO extends FileInf {
    private String userName;

    public String getName() {
        return userName;
    }

    public void setName(String name) {
        this.userName = name;
    }
}
