package com.example.demo.service.impl;

import com.example.demo.dao.DepartmentDao;
import com.example.demo.dao.DeptMemberDao;
import com.example.demo.entity.Department;
import com.example.demo.entity.DeptMember;
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
    @Autowired
    DeptMemberDao deptMemberDao;


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
        if (departmentDao.insertSelective(department) == 0) return 0;;
        return 1;
    }


    /*
    * 主键查询
    * */
    @Override
    public Department selectByPrimaryKey(Integer deptId) {
        Department department = departmentDao.selectByPrimaryKey(deptId);
        return department;
    }



    @Override
    public List<Department> selectDeptListByUserId(Integer userId) {
        List<Department> departmentList = departmentDao.selectDeptListByUserId(userId);
        return departmentList;
    }

    @Override
    public Department selectByFileCabinetId(Integer fcId) {
        Department department = departmentDao.selectByFileCabinetId(fcId);
        return department;
    }

    @Override
    public List<Department> selectByTeamId(Integer teamId) {
        List<Department> departmentList =  departmentDao.selectByTeamId(teamId);
        return departmentList;
    }

    /*
    * 检查部门名称是否重名
    * */
    @Override
    public Department selectRepeatDeptName(Department department, Team team) {
        Department result =  departmentDao.selectRepeatDeptName(department.getDeptName(),team.getTeamId());
        return result;
    }

    /*
    * 更新
    * */
    @Override
    public int updateByPrimaryKeySelective(Object... param) {
        Department department = new Department();
        department.setDeptId((Integer) param[0]);
        department.setDeptName((String) param[1]);
        department.setFcId((Integer) param[2]);
        department.setTeamId((Integer) param[3]);
        return departmentDao.updateByPrimaryKeySelective(department);
    }

    @Override
    public int updateByPrimaryKey(Department record) {
        return 0;
    }
}
