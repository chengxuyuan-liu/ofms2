package com.example.demo.service.impl;

import com.example.demo.dao.DirInfDao;
import com.example.demo.dao.FileInfDao;
import com.example.demo.entity.DirInf;
import com.example.demo.entity.UserInf;
import com.example.demo.service.DirInfService;
import com.example.demo.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class DirInfServiceImpl implements DirInfService {

    @Autowired
    DirInfDao dirInfDao;
    @Autowired
    FileInfDao fileInfDao;

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
    public List<DirInf> selectChildrenDirByDirId(Integer dirId) {

        List<DirInf> dirInfList = dirInfDao.selectChildrenDirByDirId(dirId);

        return dirInfList;
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


    /*
    删除
    */
    @Override
    public Boolean deleteByPrimaryKey(Integer dirId) {

        //初始化
        String realPath = "D:\\graduation project\\ofms\\"; //获取系统的绝对路径
        int dirResult = 0; //删除文件夹结果
        int fileResult = 0; //删除文件结果
        //获得所有子文件夹
        List<DirInf> childrenDirByDir = selectChildrenDirByDirId(dirId);


        //磁盘删除文件夹
        DirInf dirInf = dirInfDao.selectByPrimaryKey(dirId);//获得要删除的文件信息
        File target = new File(realPath + dirInf.getDirPath() + dirInf.getDirName()); //获取该文件的文件类
        FileUtil.deleteDir(target); //递归删除文件和文件夹



        //数据库删除文件夹和文件
        for (DirInf dir : childrenDirByDir) {
            fileResult = fileInfDao.deleteByDirId(dir.getDirId());  //删除文件记录 tip:如果文件夹没有文件
            dirResult = dirInfDao.deleteByPrimaryKey(dir.getDirId());  //删除文件夹记录

            if(dirResult == 0)
                return false;
        }
        return true;
    }
}
