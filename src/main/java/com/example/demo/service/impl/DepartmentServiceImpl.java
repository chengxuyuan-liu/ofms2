package com.example.demo.service.impl;

import com.example.demo.dao.DepartmentDao;
import com.example.demo.entity.Department;
import com.example.demo.entity.FileCabinet;
import com.example.demo.entity.Team;
import com.example.demo.service.DepartmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    DepartmentDao departmentDao;


    /*
    * 删除
    * */
    @Override
    public int deleteByPrimaryKey(Integer deptId) {
        return 0;
    }


    /*
    * 插入
    * */
    @Override
    public int insert(Department record) {
        return 0;
    }

    @Override
    public int insertSelective(FileCabinet fileCabinet, Team team) {
        //新部门信息
        Department department = new Department();
        department.setDeptName(fileCabinet.getFcName());
        department.setFcId(fileCabinet.getFcId());
        department.setTeamId(team.getTeamId());
        //插入记录
        return departmentDao.insertSelective(department);
    }


    /*
    * 查询
    * */
    @Override
    public Department selectByPrimaryKey(Integer deptId) {
        return null;
    }

    @Override
    public List<Department> selectDeptListByUserId(Integer userId) {
        List<Department> departmentList = departmentDao.selectDeptListByUserId(userId);
        return departmentList;
    }



    /*
    * 更新
    * */
    @Override
    public int updateByPrimaryKeySelective(Department record) {
        return 0;
    }

    @Override
    public int updateByPrimaryKey(Department record) {
        return 0;
    }
}
