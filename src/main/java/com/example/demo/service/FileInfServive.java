package com.example.demo.service;

import com.example.demo.entity.FileInf;
import com.example.demo.entity.UserInf;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public interface FileInfServive {

    /*
    查询
    */
    List<FileInf> selectFileListByFolderId(Integer dirId);
    List<FileInf> selectByFileName(String fileName,Integer fileId);  //模糊查询
    FileInf selectByPrimaryKey(Integer fileId);
    FileInf selectByFileNameAndDirId(String fileName,Integer dirId);
    /*
    删除
    */
    int deleteByDirId(Integer dirId);
    int deleteByUserId(Integer userId);

    Boolean deleteByPrimaryKey(Integer fileId);


    /*
    文件上传和下载
    */
    String fileUpload(MultipartFile file,Integer dirId,UserInf userInf);
    void download(Integer fileId, Integer dirId, HttpServletResponse response, HttpSession session);


    /*
    文件预览
   */
    String preview(Integer fileId, HttpServletResponse response);

    //更新文件
    int updateByPrimaryKeySelective(Integer fileId,Integer parentId);
    int updateFileName(Integer fileId,String fileName);

    List<FileInf> selectByFileType(List<String> type,Integer userId,Integer dirId);
}
