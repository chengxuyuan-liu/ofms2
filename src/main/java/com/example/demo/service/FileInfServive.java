package com.example.demo.service;

import com.example.demo.entity.DirInf;
import com.example.demo.entity.FileInf;
import com.example.demo.entity.UserInf;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileInfServive {

    /*
    查询
    */
    List<FileInf> selectFileListByFolderId(Integer dirId);
    List<FileInf> selectByFileName(String fileName,Integer fileId);  //模糊查询
    FileInf selectByPrimaryKey(Integer fileId);
    /*
    删除
    */
    int deleteByDirId(Integer dirId);
    Boolean deleteByPrimaryKey(Integer fileId);


    /*
    文件上传和下载
    */
    Boolean fileUpload(MultipartFile file,Integer dirId,UserInf userInf);
    Boolean download(Integer fileId,Integer dirId);


}
