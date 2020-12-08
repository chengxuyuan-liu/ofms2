package com.example.demo.service.impl;

import com.example.demo.dao.TeamDao;
import com.example.demo.entity.Team;
import com.example.demo.entity.UserInf;
import com.example.demo.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamServiceImpl implements TeamService {
    @Autowired
    TeamDao teamDao;

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
