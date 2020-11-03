package com.example.demo.service.impl;

import com.example.demo.dao.FileInfDao;
import com.example.demo.entity.FileInf;
import com.example.demo.service.FileInfServive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileInfServiceImpl implements FileInfServive {

    @Autowired
    FileInfDao fileInfDao;

    @Override
    public List<FileInf> selectFileListByFolderId(Integer dirId) {
        List<FileInf> result = fileInfDao.selectFileListByFolderId(dirId);
        return result;
    }

    @Override
    public int deleteByDirId(Integer dirId) {

        int result = fileInfDao.deleteByDirId(dirId);
        return result;
    }
}
