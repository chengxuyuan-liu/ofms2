package com.example.demo.util;

import java.io.File;

public class FileUtil {

    /*
    递归删除文件和文件下的子文件
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
}