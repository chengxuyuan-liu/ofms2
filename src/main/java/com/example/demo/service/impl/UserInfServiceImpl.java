package com.example.demo.service.impl;

import com.example.demo.dao.DirInfDao;
import com.example.demo.dao.FileCabinetDao;
import com.example.demo.dao.UserInfDao;
import com.example.demo.entity.Department;
import com.example.demo.entity.FileCabinet;
import com.example.demo.entity.Team;
import com.example.demo.entity.UserInf;
import com.example.demo.service.FileCabinetService;
import com.example.demo.service.DirInfService;
import com.example.demo.service.TeamService;
import com.example.demo.service.UserInfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Service
public class UserInfServiceImpl implements UserInfService {

    @Autowired
    UserInfDao userInfDao;
    @Autowired
    DirInfService dirInfService;
    @Autowired
    DirInfDao dirInfDao;

    @Autowired
    FileCabinetService deptInfService;
    @Autowired
    FileCabinetService fileCabinetService;
    @Autowired
    FileCabinetDao fileCabinetDao;

    @Autowired
    TeamService teamService;



    //判断用户空间
    @Override
    public Boolean judgeSpace(UserInf userInf) {
        if (userInf.getUsedSpace().compareTo(userInf.getMaxSpace()) > 0) return false;
        return true;
    }

    /*
        验证登录
        */
    @Override
    public UserInf verifyLogin(String username, String password) {
        UserInf userInf = userInfDao.selectByUsername(username);
        if (userInf != null) {
            if (password.equals(userInf.getPassword())) {
                return userInf;
            }
        }
        return null;
    }

    @Override
    public int deleteByPrimaryKey(Integer userId) {
        int result = userInfDao.deleteByPrimaryKey(userId);
        return result;
    }

    @Override
    public int insert(UserInf record) {
        return 0;
    }


    /*
    注册
    */
    @Override
    public UserInf insertSelective(UserInf userInf) {
        //注册时间
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        //封装数据
        userInf.setRegisterTime(date);
        userInf.setStatus(1);
        if(userInf.getUserType() == 1){
            userInf.setMaxSpace(new BigInteger("0"));
        }
        //调用dao,插入用户表
        int sign = userInfDao.insertSelective(userInf);
        return userInf;
    }



    @Override
    public UserInf selectByPrimaryKey(Integer userId) {
        UserInf userInf = userInfDao.selectByPrimaryKey(userId);
        return userInf;
    }

    @Override
    public UserInf selectByUserPhone(String userPhone) {
        UserInf userInf = userInfDao.selectByUserPhone(userPhone);
        return userInf;
    }

    @Override
    public List<UserInf> selectListByUserName(String userName) {
        List<UserInf> userInfList = userInfDao.selectListByUsername(userName);
        return userInfList;
    }

    @Override
    public UserInf selectByEmail(String email) {
        UserInf userInf = userInfDao.selectByEmail(email);
        return userInf;
    }


    @Override
    public List<UserInf> selectAll() {
        List<UserInf> userInfList = userInfDao.selectAll();
        return userInfList;
    }



    @Override
    public int updateByPrimaryKeySelective(UserInf record) {
        int result = userInfDao.updateByPrimaryKeySelective(record);
        return result;
    }

    @Override
    public int updateSpaceWhenNewDept(UserInf record) {
        //usedSpace + 1G
        Long increment = Long.valueOf(1024*1024*1024);
        BigInteger count = record.getUsedSpace().add(new BigInteger(increment.toString()));
        record.setUsedSpace(count);
        int result = userInfDao.updateByPrimaryKeySelective(record);
        return result;
    }

    @Override
    public int updateSpaceWhenDeleteDept(UserInf record,FileCabinet fileCabinet) {
        BigInteger count = record.getUsedSpace().subtract(fileCabinet.getMaxSpace());
        record.setUsedSpace(count);
        return userInfDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateSpaceWhenEditDept(UserInf record,FileCabinet fileCabinet,BigInteger maxSpace) {
        //usedSpace - dept_maxSpace + maxSpace
        BigInteger count = record.getUsedSpace().subtract(fileCabinet.getMaxSpace()).add(maxSpace);
        record.setUsedSpace(count);
        int result = userInfDao.updateByPrimaryKeySelective(record);
        return result;
    }

    @Override
    public int updateByPrimaryKey(UserInf record) {
        return 0;
    }



}
