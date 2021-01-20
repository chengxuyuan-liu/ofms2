package com.example.demo.util;

public class PreviewUtil {

    private  final static String SF = "docx,doc,xls,xlsx,ppt,pptx,txt,jpg,png,pdf";
    private  final static String VIDEO_AND_AUDIO= "mp4,mp3";
    public static Boolean isDocument(String fileName){
        String fileType = fileName.substring(fileName.lastIndexOf(".")+1);
        System.out.println("预览文件的格式为："+fileType);
        return SF.indexOf(fileType) >= 0;
    }

    public static Boolean isVideoOrAudio(String fileName){
        String fileType = fileName.substring(fileName.lastIndexOf(".")+1);
        System.out.println("预览文件的格式为："+fileType);
        return VIDEO_AND_AUDIO.indexOf(fileType) >= 0;
    }
}
