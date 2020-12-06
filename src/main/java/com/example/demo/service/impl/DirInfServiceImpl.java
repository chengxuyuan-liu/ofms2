package com.example.demo.service.impl;

import com.example.demo.dao.DirInfDao;
import com.example.demo.dao.FileInfDao;
import com.example.demo.entity.DirInf;
import com.example.demo.entity.UserInf;
import com.example.demo.service.DirInfService;
import com.example.demo.util.DateUtil;
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
    public List<DirInf> selectDirListByParentDirId(Integer dirId) {
        List<DirInf> dirInfList = dirInfDao.selectDirListByParentDirId(dirId);
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
    public DirInf selectDirByDirName(String dirName, Integer parentId) {
        return null;
    }


    @Override
    public DirInf selectRootChildrenDirByUserId(Integer userId,String dirName) {
        DirInf dirInf = dirInfDao.selectRootChildrenDirByUserId(userId,dirName);
        return dirInf;
    }

    @Override
    public DirInf selectRootDirByUserId(Integer userId) {
        DirInf dirInf = dirInfDao.selectRootDirByUserId(userId);
        return dirInf;
    }

    @Override
    public DirInf selectByPrimaryKey(Integer dirId) {
        DirInf dirInf = dirInfDao.selectByPrimaryKey(dirId);
        return dirInf;
    }

    @Override
    public DirInf selectDirByFileId(Integer fileId) {
        DirInf dirInf = dirInfDao.selectDirByFileId(fileId);
        return dirInf;
    }

    @Override
    public List<DirInf> selectByDirName(String dirName, Integer dirId) {

        List<DirInf> dirInf = dirInfDao.selectByDirName(dirName,dirId);
        return dirInf;
    }

    /*
   新建文件夹
    */
    @Override
    public DirInf insertSelective(String dirName, Integer parentDirId, UserInf user) {

        String realPath = "D:\\graduation project\\ofms"; //获取系统的绝对路径
        File realDir;
        File myDirFile;
        int result;
        DirInf newDir = new DirInf();

        if(parentDirId!=null) {
            DirInf fatherDir = dirInfDao.selectByPrimaryKey(parentDirId);//获得父文件夹
            //构建新文件的属性

            //以文件名查询文件夹
            DirInf dirInf = dirInfDao.selectDirByDirName(dirName,parentDirId);
            //判断文件名是否重复，若重复则 重构：文件名+当前日期
            if(dirInf != null) dirName = dirName+"_"+DateUtil.getNowDateForString();

            newDir.setDirName(dirName);
            newDir.setParentDir(fatherDir.getDirId());
            newDir.setUserId(user.getUserId());         //文件夹所属
            newDir.setDirPath(fatherDir.getDirPath()
                    + fatherDir.getDirName() + "\\");
            realDir = new File(realPath + fatherDir.getDirPath() + fatherDir.getDirName()
                    + "\\" + dirName);    //创建 新文件夹 的  文件类
            //如果文件不存在
            if(!realDir.exists()) {
                realDir.mkdir();    //磁盘内创建相应的文件夹
            }

            //记录写入数据库
            result = dirInfDao.insertSelective(newDir);

        } else{

            //用户根目录
            newDir.setDirName(user.getUserPhone().toString());
            newDir.setUserId(user.getUserId());         //文件夹所属
            newDir.setDirPath("\\");
            //记录写入数据库
            result = dirInfDao.insertSelective(newDir);

            //”我的文件“
//            DirInf myDir = new DirInf();
//            myDir.setDirName("我的文件");
//            myDir.setParentDir(newDir.getDirId());
//            myDir.setUserId(user.getUserId());          //文件夹所属
//            myDir.setDirPath(newDir.getDirPath()+newDir.getDirName()+"\\");

            //外存中新建文件夹
            realDir = new File(realPath + newDir.getDirPath() + newDir.getDirName());    //创建 新文件夹 的  文件类
//            myDirFile = new File(realPath + myDir.getDirPath() + myDir.getDirName());
//            if (result != 0)
//                result = dirInfDao.insertSelective(myDir);

            //如果文件不存在
            if(!realDir.exists()) {
                realDir.mkdir();    //磁盘内创建相应的文件夹
//                if (!myDirFile.exists()) {
//                    myDirFile.mkdir();
//                }
            }



        }


        return newDir;
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

    @Override
    public int deleteByUserId(Integer userId) {
        int result = dirInfDao.deleteByUserId(userId);
        return result;
    }

    /*
    下载文件夹
    */


}
