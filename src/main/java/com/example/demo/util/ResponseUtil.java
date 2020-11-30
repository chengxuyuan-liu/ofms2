package com.example.demo.util;

import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

public class ResponseUtil {


    @ResponseBody
    public static void responFileToBrower(HttpServletResponse response,String filePaht) throws Exception {

        //输入流
        byte[] buffer = new byte[1024];     //缓存区
        FileInputStream fis = null;         //文件输入流
        BufferedInputStream bis = null;     //缓存输入流

        //输出流
        OutputStream os = null;  //输出流

        //取出文件，返回给浏览器
        String headerFileName = filePaht.substring(filePaht.lastIndexOf("\\")+1);     //下载文件命名
        System.out.println(headerFileName);
        //设置编码
        headerFileName = java.net.URLEncoder.encode(headerFileName, "UTF-8");           //转UTF-8
        headerFileName = new String(headerFileName.getBytes(), "iso-8859-1");     //UTF-8 转 ios
        //设置消息头
        response.setContentType("application/force-download");      //
        response.setHeader("Content-Disposition", "attachment;fileName=" + headerFileName);
        //获得输入流
        File file = new File(filePaht);
        fis = new FileInputStream(file);
        bis = new BufferedInputStream(fis);

        //获得输出流

        os = response.getOutputStream();

        //开始输出
        int i = bis.read(buffer);
        while (i != -1) {
            os.write(buffer);
            i = bis.read(buffer);
        }
        bis.close();
        fis.close();
        os.flush();
        os.close();
    }
}
