package com.example.demo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class FileUtil {

    /*
    递归删除硬盘下的文件和文件下的子文件
    */
    public static Boolean deleteDir(File dir) {
        if (dir.isDirectory())  //是否是文件夹
        {
            String[] children = dir.list();  //获得文件的子文件夹

            for (int i = 0; i < children.length; i++)
            {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success)
                {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    // 复制硬盘下的某个目录及目录下的所有子目录和文件到新文件夹
    public static void copyFolder(File oldFile, File target) {

        try {

            String movePath =  target.getAbsolutePath()+"/"+oldFile.getName();//在新文件夹中如果不存在旧文件夹，就创建一个；
            System.out.println("目标文件夹："+target.getAbsolutePath());
            System.out.println("新文件夹："+target.getAbsolutePath()+"\\"+oldFile.getName());
            (new File(movePath)).mkdirs();
            // 读取整个文件夹的内容到file字符串数组，下面设置一个游标i，不停地向下移开始读这个数组
            String[] file = oldFile.list();
            // 要注意，这个temp仅仅是一个临时文件指针
            // 整个程序并没有创建临时文件
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                // 如果oldPath以路径分隔符/或者\结尾，那么则oldPath/文件名就可以了
                if (oldFile.getAbsolutePath().endsWith(File.separator)) {
                    System.out.println("有分隔符");
                    temp = new File(oldFile + file[i]);         //文件夹
                } else {
                    System.out.println("无有分隔符");
                    temp = new File(oldFile + File.separator + file[i]);    //文件
                }

                // 如果游标遇到文件
                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    // 复制并且改名
                    FileOutputStream output = new FileOutputStream(movePath
                            + "\\" + (temp.getName()).toString());
                    byte[] bufferarray = new byte[1024 * 64];
                    int prereadlength;
                    while ((prereadlength = input.read(bufferarray)) != -1) {
                        output.write(bufferarray, 0, prereadlength);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                // 如果游标遇到文件夹
                if (temp.isDirectory()) {
                    copyFolder(new File(oldFile.getAbsolutePath() + "\\" + file[i]),
                               new File(target.getAbsolutePath()+ "\\"+ oldFile.getName()));
                }
            }
        } catch (Exception e) {
            System.out.println("复制整个文件夹内容操作出错");
        }
    }

    //移动硬盘下的文件夹和文件
    public static void moveFolder(File oldFile, File target) {
        // 先复制文件
        copyFolder(oldFile, target);
        // 则删除源文件，以免复制的时候错乱
        deleteDir(oldFile);
    }

}