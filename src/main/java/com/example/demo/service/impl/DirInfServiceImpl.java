package com.example.demo.service.impl;

import com.example.demo.dao.DirInfDao;
import com.example.demo.entity.DirInf;
import com.example.demo.entity.UserInf;
import com.example.demo.service.DirInfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class DirInfServiceImpl implements DirInfService {

    @Autowired
    DirInfDao dirInfDao;


    /*
     查询
    */
    @Override
    public List<DirInf> selectDirListByDirId(Integer dirId) {

        List<DirInf> dirInfList = dirInfDao.selectDirListByDirId(dirId);

        return dirInfList;
    }

    @Override
    public List<DirInf> selectParentDirByDirId(Integer dirId) {

        List<DirInf> accessPath = dirInfDao.selectParentDirByDirId(dirId);
        return accessPath;
    }

    @Override
    public DirInf selectRootDirByUserId(Integer userId) {

        DirInf dirInf = dirInfDao.selectRootDirByUserId(userId);

        return dirInf;
    }

    /*
     插入
    */
    @Override
    public int insertSelective(String dirName, Integer parentDirId, UserInf user) {

        String realPath = "D:\\graduation project\\ofms\\"; //获取系统的绝对路径
        File realDir;
        DirInf newDir = new DirInf();

        if(parentDirId!=null) {
            DirInf fatherDir = dirInfDao.selectByPrimaryKey(parentDirId);//获得父文件夹
            //构建新文件的属性

            newDir.setDirName(dirName);
            newDir.setParentDir(fatherDir.getDirId());
            newDir.setUserId(user.getUserId());
            newDir.setDirPath(fatherDir.getDirPath()
                    + fatherDir.getDirName() + "\\");
            realDir = new File(realPath + fatherDir.getDirPath() + fatherDir.getDirName()
                    + "\\" + dirName);    //创建 新文件夹 的  文件类
        } else{
            newDir.setDirName(user.getUserPhone().toString());
            newDir.setUserId(user.getUserId());
            //新用户根文件夹
            newDir.setDirPath("\\");
            //新用户根文件夹
            realDir = new File(realPath + "\\" + user.getUserPhone());    //创建 新文件夹 的  文件类
        }

        //如果文件不存在
        if(!realDir.exists()) {
            realDir.mkdir();    //磁盘内创建相应的文件夹
        }

        //记录写入数据库
        int result = dirInfDao.insertSelective(newDir);

        return result;
    }

    @Override
    public int deleteByPrimaryKey(Integer dirId) {

        int result = dirInfDao.deleteByPrimaryKey(dirId);
        return result;
    }
}
