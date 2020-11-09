package com.example.demo.service.impl;

import com.example.demo.dao.FileInfDao;
import com.example.demo.entity.DirInf;
import com.example.demo.entity.FileInf;
import com.example.demo.entity.UserInf;
import com.example.demo.service.DirInfService;
import com.example.demo.service.FileInfServive;
import com.example.demo.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Service
public class FileInfServiceImpl implements FileInfServive {

    @Autowired
    FileInfDao fileInfDao;
    @Autowired
    DirInfService dirInfService;

    /*
    查询
    */
    @Override
    public List<FileInf> selectFileListByFolderId(Integer dirId) {
        List<FileInf> result = fileInfDao.selectFileListByFolderId(dirId);
        return result;
    }

    @Override
    public FileInf selectByPrimaryKey(Integer fileId) {
        FileInf fileInf = fileInfDao.selectByPrimaryKey(fileId);
        return fileInf;
    }

    /*
    删除
    */
    @Override
    public int deleteByDirId(Integer dirId) {

        int result = fileInfDao.deleteByDirId(dirId);
        return result;
    }

    @Override
    public Boolean deleteByPrimaryKey(Integer fileId) {

        //外存删除相应文件
        //获得获得文件的存储路径
        String systemPath = "D:\\graduation project\\ofms\\";
        DirInf dirInf = dirInfService.selectDirByFileId(fileId);
        FileInf fileInf = fileInfDao.selectByPrimaryKey(fileId);
        String filePath = systemPath+ dirInf.getDirPath()+dirInf.getDirName();

        //创建文件类
        File file = new File(filePath,fileInf.getFileName());

        //删除文件
        if (file.exists()) {
            file.delete();
        }


        //数据库删除记录
        int result = fileInfDao.deleteByPrimaryKey(fileId);
        if (result != 0) return true;
        return false;
    }

    @Override
    public List<FileInf> selectByFileName(String fileName, Integer fileId) {
        List<FileInf> fileInf = fileInfDao.selectByFileName(fileName,fileId);
        return fileInf;
    }

    /*
    文件上传
    */
    @Override
    public Boolean fileUpload(MultipartFile file, Integer dirId, UserInf userInf) {
        DirInf dirInf = dirInfService.selectByPrimaryKey(dirId); //获取目标文件夹

        String systemPath = "D:\\graduation project\\ofms";
        //获取上传文件的信息
        String fileOriginalName = file.getOriginalFilename();  //文件的原始名
        String fileName = fileOriginalName;
        String fileType = fileOriginalName.substring(fileOriginalName.lastIndexOf(".") + 1); //获取文件类型
        Double fileSize = (double) file.getSize(); //获取文件的大小，单位为字节
        String fileUnit = "B";      //文件的初始单位

        //单位转换
        if (fileSize >= 1024) {
            //fileSize换算成KB，只保留整数
            fileSize = Math.floor(fileSize / 1024);
            fileUnit = "KB";
            if (fileSize >= 1024) {
                //fileSize换算成MB,保留两位小数点
                fileSize = Math.floor(fileSize / 1024);
                fileUnit = "MB";
                if (fileSize >= 1024) {
                    //fileSize换算成GB，保留两位小数点
                    fileSize = Math.floor(fileSize / 1024);
                    fileUnit = "GB";

                }
            }
        }

        //插入数据库文件记录
        FileInf fileInf = new FileInf();
        fileInf.setFileName(fileName);//获取文件名
        fileInf.setFileSize(fileSize);//获取文件大小，以字节为单位
        fileInf.setFileUnit(fileUnit);//获得文件转换后的单位
        fileInf.setFileType(fileType);//获得文件的类型
        fileInf.setFileUploadTime(DateUtil.getNowDate());//获得上传时间
        fileInf.setDirId(dirId);//文件夹id
        fileInf.setUserId(userInf.getUserId());//用户id
        int resutl1 = fileInfDao.insertSelective(fileInf);
        if(resutl1 == 0)  return false;

        //上传文件到外存
        //判断文件是否存在
        if (!file.isEmpty() && file.getSize() > 0) {
            //该文件的原始名
            //获得保存的路径
            String realPath = systemPath+dirInf.getDirPath()+"\\"+dirInf.getDirName();
            System.out.println(realPath);
            //使用UUID重新命名上传的文件,上传人_UUID_原始文件名；
            //上传文件
            try {
                file.transferTo(new File(realPath, fileName));
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;

    }

    /*
    文件下载
    */
    @Override
    public Boolean download(Integer fileId, Integer dirId) {

        //获取文件信息
        //获取文件夹信息

        //获取文件所在的路径
        //获取文件的文件类

        //设置消息头
        //设置浏览器的打开方式
        //设置以流的形式返回
        //返回响应实体

        return null;
    }
}
