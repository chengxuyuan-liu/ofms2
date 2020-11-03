package com.example.demo.service;

import com.example.demo.entity.DirInf;
import com.example.demo.entity.FileInf;

import java.util.List;

public interface FileInfServive {

    //查询
    List<FileInf> selectFileListByFolderId(Integer dirId);



    /*
    删除
    */
    int deleteByDirId(Integer dirId);

}
