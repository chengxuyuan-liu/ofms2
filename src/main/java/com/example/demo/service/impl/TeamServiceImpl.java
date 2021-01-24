package com.example.demo.service.impl;

import com.example.demo.dao.DeptMemberDao;
import com.example.demo.dao.TeamDao;
import com.example.demo.entity.DeptMember;
import com.example.demo.entity.Team;
import com.example.demo.entity.UserInf;
import com.example.demo.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamServiceImpl implements TeamService {
    @Autowired
    TeamDao teamDao;
    @Autowired
    DeptMemberDao deptMemberDao;

    @Override
    public int deleteByPrimaryKey(Integer teamId) {
        return 0;
    }

    @Override
    public int insert(Team record) {
        return 0;
    }

    @Override
    public int insertSelective(Team team, UserInf userInf) {
        team.setUserId(userInf.getUserId());
        int result = teamDao.insertSelective(team);

        //插入团队用户成员信息
        DeptMember deptMember = new DeptMember();
        deptMember.setDeptId(null);
        deptMember.setUserId(userInf.getUserId());
        deptMember.setTeamId(team.getTeamId());
        deptMemberDao.insertSelective(deptMember);

        return result;
    }

    @Override
    public Team selectByPrimaryKey(Integer teamId) {
        Team team = teamDao.selectByPrimaryKey(teamId);
        return team;
    }

    @Override
    public Team selectByUserId(UserInf userInf) {
        Team team = teamDao.selectByUserId(userInf.getUserId());
        return team;
    }

    @Override
    public int updateByPrimaryKeySelective(Team record) {
        return 0;
    }

    @Override
    public int updateByPrimaryKey(Team record) {
        return 0;
    }
}
