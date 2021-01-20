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
    public List<DirInf> selectDirListByParentDirId(Integer dirId) {
        List<DirInf> dirInfList = dirInfDao.selectDirListByParentDirId(dirId);
        return dirInfList;
    }

    @Override
    public List<DirInf> selectParentDirByDirId(Integer dirId) {
        List<DirInf> accessPath = dirInfDao.selectParentDirByDirId(dirId);
        return accessPath;
    }


    /*
    *
    * */
    @Override
    public List<DirInf> selectChildrenDirByDirId(Integer dirId) {
        List<DirInf> dirInfList = dirInfDao.selectChildrenDirByDirId(dirId);
        return dirInfList;
    }

    @Override
    public DirInf selectDirByDirName(String dirName, Integer parentId) {
        DirInf dirInf = dirInfDao.selectDirByDirName(dirName,parentId);
        return dirInf;
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
        //如果父文件夹id不为空（通过创建文件夹按钮创建），如果为空（创建新用户时，创建文件夹）
        if(parentDirId!=null) {
            DirInf fatherDir = dirInfDao.selectByPrimaryKey(parentDirId);//获得父文件夹
            //判断重名以及名字重构
            String item = dirName;
            int i = 0;
            while(true) {
                //以文件名查询文件夹,查看是否已存在这个文件夹名
                DirInf dirInf = dirInfDao.selectDirByDirName(dirName, parentDirId);
                //判断文件名是否重复，若重复则 重构：文件名+当前日期
                if (dirInf != null) {
                    i++;
                    dirName = item + "(" +i+")";
                } else {
                    break;
                }
            }
            //封装新文件夹的属性
            newDir.setDirName(dirName);
            newDir.setParentDir(fatherDir.getDirId());
            newDir.setUserId(user.getUserId());         //文件夹所属
            newDir.setDirPath(fatherDir.getDirPath()
                    + fatherDir.getDirName() + "\\");
            realDir = new File(realPath + fatherDir.getDirPath() + fatherDir.getDirName()
                    + "\\" + dirName);    //创建 新文件夹 的 文件类
            //如果文件不存在，就创建文件夹
            if(!realDir.exists()) {
                System.out.println("在硬盘中创建新文件夹："+realDir.getAbsolutePath());
                realDir.mkdirs();    //磁盘内创建相应的文件夹
            }
            //记录写入数据库
            if(dirInfDao.insertSelective(newDir) == 0) return null;
        } else{
            //用户根目录
            newDir.setDirName(user.getEmail().toString());
            newDir.setUserId(user.getUserId());         //文件夹所属
            newDir.setDirPath("\\");
            //记录写入数据库
            result = dirInfDao.insertSelective(newDir);
            //硬盘中中新建文件夹
            realDir = new File(realPath + newDir.getDirPath() + newDir.getDirName());    //创建 新文件夹 的  文件类
            //如果文件不存在
            if(!realDir.exists()) {
                realDir.mkdir();    //磁盘内创建相应的文件夹
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
    * 移动文件夹
    * */
    @Override
    public String updateByPrimaryKeySelective(Integer dirId, Integer parentId) {
        String systemPath = "D:\\graduation project\\ofms";
        //硬盘移动文件
        DirInf dirInf = dirInfDao.selectByPrimaryKey(dirId); //文件夹
        DirInf fatherDir = dirInfDao.selectByPrimaryKey(dirInf.getParentDir()); //父文件夹
        DirInf dirInf2 = dirInfDao.selectByPrimaryKey(parentId); //目标文件夹
        File file = new File(systemPath+dirInf.getDirPath()+dirInf.getDirName()+"\\"); //文件夹文件类
        File file2 = new File(systemPath+dirInf2.getDirPath()+dirInf2.getDirName()+"\\"); //目标文件夹文件类
        System.out.println(systemPath+dirInf.getDirPath()+dirInf.getDirName()+"\\");

        //判断目标文件中是否存在重名文件夹
        DirInf repeatDir = dirInfDao.selectDirByDirName(dirInf.getDirName(),dirInf2.getDirId());
        if(repeatDir != null){
            return "文件夹已存在，操作失败！";
        }

        //如果路径相同不移动
        if(!fatherDir.getDirId().equals(dirInf2.getDirId())){
            System.out.println("路径不同");
            FileUtil.moveFolder(file,file2);
            //数据库移动文件
            List<DirInf> dirInfList = dirInfDao.selectChildrenDirByDirId(dirId);
            DirInf item  = dirInf2; //当前要修改文件夹的父文件夹
            for (DirInf inf : dirInfList) {
                String newPath = item.getDirPath()+item.getDirName()+"\\"; //构建新路径
                //封装item
                DirInf updateDirInf = new DirInf();
                updateDirInf.setDirId(inf.getDirId());
                updateDirInf.setDirName(inf.getDirName());
                updateDirInf.setParentDir(item.getDirId());
                updateDirInf.setDirPath(newPath);
                if(dirInfDao.updateByPrimaryKeySelective(updateDirInf)!=1) return "保存失败";    //数据持久化
                item = updateDirInf; //更新父文件夹
            }
        }
        return "保存成功";
    }

    /*
    修改文件夹
    */
    @Override
    public String updateDirName(Integer dirId,String dirName) {

        String systemPath = "D:\\graduation project\\ofms";
        //修改硬盘文件名
        DirInf dirInf1 = dirInfDao.selectByPrimaryKey(dirId); //原文件所在文件夹
        String dirPhat = systemPath+dirInf1.getDirPath();
        File file = new File(dirPhat+dirInf1.getDirName()); //原文件文件类
        File newFile = new File(dirPhat+dirName);
        System.out.println(dirPhat+dirInf1.getDirName());
        System.out.println(dirPhat+dirName);

        //校验新文件名是否在硬盘中是否存在，存在则返回对应信息
        if(newFile.exists()){
            return "文件已存在!";       //文件已存在
        }
        //改名
        if(!file.renameTo(newFile)){
            return "修改失败!";
        }
        //数据库修改
        DirInf dirInf = new DirInf();
        dirInf.setDirId(dirId);
        dirInf.setDirName(dirName);
        int result = dirInfDao.updateByPrimaryKeySelective(dirInf);
        if(result == 0) {
            return "修改失败!";
        }
        return "修改成功!";
    }

}
