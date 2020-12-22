package com.example.demo.service.impl;

import com.example.demo.aop.ArchivesLog;
import com.example.demo.dao.UserLogDao;
import com.example.demo.entity.PageRequest;
import com.example.demo.entity.PageResult;
import com.example.demo.entity.UserLog;
import com.example.demo.service.UserLogService;
import com.example.demo.util.PageUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class UserLogServiceImpl implements UserLogService {

    @Autowired
    UserLogDao userLogDao;

    @Override
    public int deleteByPrimaryKey(Integer logId) {

        int result = userLogDao.deleteByPrimaryKey(logId);
        return result;
    }

    @Override
    public int insert(UserLog record) {
        return 0;
    }

    @Override
    public int insertSelective(UserLog record) {
        Date date = new Date();
        record.setOperateDate(date);
        int result = userLogDao.insertSelective(record);
        return result;
    }

    @Override
    public UserLog selectByPrimaryKey(Integer logId) {
        return null;
    }

    @Override
    public int updateByPrimaryKeySelective(UserLog record) {
        return 0;
    }

    @Override
    public int updateByPrimaryKey(UserLog record) {
        return 0;
    }

    @Override
    public PageResult findPage(PageRequest pageRequest) {
        return PageUtils.getPageResult(pageRequest, getPageInfo(pageRequest));
    }

    private PageInfo<UserLog> getPageInfo(PageRequest pageRequest) {
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        String oderBy = "operate_date"+" desc";
        PageHelper.startPage(pageNum, pageSize,oderBy);
        List<UserLog> userLog = userLogDao.seletePage();
        return new PageInfo<UserLog>(userLog);
    }
}
