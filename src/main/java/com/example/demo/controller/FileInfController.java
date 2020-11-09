package com.example.demo.controller;

import com.example.demo.entity.DirInf;
import com.example.demo.entity.FileInf;
import com.example.demo.entity.UserInf;
import com.example.demo.service.DirInfService;
import com.example.demo.service.FileInfServive;
import com.example.demo.util.ResponseUtil;
import com.example.demo.util.ZipCompress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.zip.ZipOutputStream;

@Controller
public class FileInfController {

    @Autowired
    FileInfServive fileInfServive;
    @Autowired
    DirInfService dirInfService;

    /*
    文件上传
    */
    @ResponseBody
    @RequestMapping("/fileUpload")
    public String fileUpload(MultipartFile uploadfile, Integer dirId, HttpSession session){

        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");
        fileInfServive.fileUpload(uploadfile,dirId,userInf);
        return "OK";
    }

    /*
    文件下载
    */
    @RequestMapping(value = "/download")
    public void download(Integer fileId,Integer dirId,HttpServletResponse response) throws Exception {
        //调用业务
        String systemPath = "D:\\graduation project\\ofms";

        //输入流
        byte[] buffer = new byte[1024];     //缓存区
        FileInputStream fis = null;         //文件输入流
        BufferedInputStream bis = null;     //缓存输入流

        //输出流
        OutputStream os = null;  //输出流

        DirInf dirInf = dirInfService.selectByPrimaryKey(dirId); //文件夹信息

        //判断是文件还是文件夹
        if (fileId != null) {   //如果是文件
            FileInf fileInf = fileInfServive.selectByPrimaryKey(fileId);
            String filePaht = systemPath + dirInf.getDirPath() + dirInf.getDirName()+"\\"+ fileInf.getFileName();     //文件所在路径

            //返回文件给浏览器
            ResponseUtil.responFileToBrower(response,filePaht);

        } else {
            /*思路：源文件夹通过ZipOutputStream加工获得文件夹的Zip压缩文件存放在临时文件夹，再通过缓存输入流把文件读出来
            最后通过ServletOutputStream响应给页面*/

            //压缩文件，存放到临时文件夹
            String temp = "D:\\graduation project\\ofms\\Tmpe";  //临时文件夹路径
            String zipFileName = temp + "\\" + dirInf.getDirName() + ".zip";      //输出的zip文件 路径 和 名称
            ZipCompress zipCom = new ZipCompress(zipFileName, systemPath + dirInf.getDirPath() + dirInf.getDirName());
            try {
                zipCom.zip();
            } catch (Exception e) {
                e.printStackTrace();
            }

            //返回文件给浏览器
            ResponseUtil.responFileToBrower(response,zipFileName);

        }

    }


    /*
    文件删除
    */
    @ResponseBody
    @RequestMapping("/deleteFile")
    public String deleterFile(Integer fileId, HttpSession session){
        Boolean result = fileInfServive.deleteByPrimaryKey(fileId);
        if (result)  return "OK";
        return "FALSE";
    }
}
