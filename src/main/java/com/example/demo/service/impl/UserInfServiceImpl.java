package com.example.demo.service.impl;

import com.example.demo.dao.UserInfDao;
import com.example.demo.entity.UserInf;
import com.example.demo.service.DirInfService;
import com.example.demo.service.UserInfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.util.calendar.LocalGregorianCalendar;

import java.text.SimpleDateFormat;
import java.util.Date;


@Service
public class UserInfServiceImpl implements UserInfService {

    @Autowired
    UserInfDao userInfDao;
    @Autowired
    DirInfService dirInfService;

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
        return 0;
    }

    @Override
    public int insert(UserInf record) {
        return 0;
    }


    /*
    注册
    */
    @Override
    public int insertSelective(Integer userType,String username, String email, String password, Integer phone) {

        //注册时间
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        //封装数据
        UserInf userInf = new UserInf();
        userInf.setUsername(username);
        userInf.setEmail(email);
        userInf.setPassword(password);
        userInf.setUserPhone(phone);
        userInf.setRegisterTime(date);
        userInf.setStatus(1);
        userInf.setUserType(userType);
        //调用dao
        int sign = userInfDao.insertSelective(userInf);

        System.out.println("用户id:"+userInf.getUserId());

        //新建用户根文件夹
        if(sign != 0) {
           int sign2 =  dirInfService.insertSelective(phone.toString(), null, userInf);
           if(sign2 == 0)
               sign = 0;
        }
        return sign;
    }


    @Override
    public UserInf selectByPrimaryKey(Integer userId) {
        UserInf userInf = userInfDao.selectByPrimaryKey(userId);
        return userInf;
    }

    @Override
    public int updateByPrimaryKeySelective(UserInf record) {
        return 0;
    }

    @Override
    public int updateByPrimaryKey(UserInf record) {
        return 0;
    }
}
