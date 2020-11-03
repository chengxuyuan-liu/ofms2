package com.example.demo.service.impl;

import com.example.demo.dao.DeptInfDao;
import com.example.demo.entity.DeptInf;
import com.example.demo.service.DeptInfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeptInfServiceImpl implements DeptInfService {

    @Autowired
    private DeptInfDao deptInfDao;

    @Override
    public DeptInf selectByPrimaryKey(Integer deptId) {
        return deptInfDao.selectByPrimaryKey(deptId);
    }
}
