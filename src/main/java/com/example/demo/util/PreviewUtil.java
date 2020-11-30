package com.example.demo.util;

public class PreviewUtil {

     private  final static String sf = "docx,xlsx,pptx,txt,jpg,png";

    public static Boolean check(String fileName){
        String fileType = fileName.substring(fileName.lastIndexOf(".")+1);
        System.out.println("预览文件的格式为："+fileType);
        return sf.indexOf(fileType) >= 0;
    }
}
