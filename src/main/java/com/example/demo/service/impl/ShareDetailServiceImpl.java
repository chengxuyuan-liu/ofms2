package com.example.demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.ShareDetailDao;
import com.example.demo.dao.ShareingDao;
import com.example.demo.entity.FileInf;
import com.example.demo.entity.ShareDetail;
import com.example.demo.entity.Shareing;
import com.example.demo.service.FileInfServive;
import com.example.demo.service.ShareDetailService;
import com.example.demo.service.ShareingService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShareDetailServiceImpl implements ShareDetailService {
    @Autowired
    ShareDetailDao shareDetailDao;
    @Autowired
    FileInfServive fileInfServive;
    @Autowired
    ShareingDao shareingDao;

    @Override
    public List<ShareDetail> selectAll(Integer userId) {
        List<ShareDetail> list = shareDetailDao.selectAll(userId);
        return list;
    }

    @Override
    public String add(Integer fileId, String memberId, Integer memberNum,
                      String describe,Integer userId) {
        //查询文件信息
        FileInf fileInf = fileInfServive.selectByPrimaryKey(fileId);
        //处理成员id
        JSONObject jsonObject = JSONObject.parseObject(memberId);
        JSONArray jsonElements = jsonObject.getJSONArray("memberId");
        List<Integer> memberIdList = jsonElements.toJavaList(Integer.class);

        //插入共享详情表数据，返回详情id(描述、文件名、总人数、文件id、用户id)
        ShareDetail shareDetail = new ShareDetail();
        shareDetail.setFileName(fileInf.getFileName());
        shareDetail.setAllNum(memberNum);
        shareDetail.setFileId(fileId);
        shareDetail.setShareDescribe(describe);
        shareDetail.setUserId(userId);
        if(shareDetailDao.insertSelective(shareDetail)==0) return "添加失败！";

        //插入共享表数据，详情id，循环选中成员，状态默认为1
        for (Integer integer : memberIdList) {
            Shareing shareing = new Shareing();
            shareing.setSdId(shareDetail.getSdId());
            shareing.setMemberId(integer);
            if(shareingDao.insertSelective(shareing)==0) return "添加失败！";
        }
        return "添加成功！";
    }


    /*
     * 删除
     * */
    @Override
    public String delete(String[] sdId) {
        //查询
        if (sdId != null) {
            for (int i = 0; i < sdId.length; i++) {
                shareDetailDao.deleteByPrimaryKey(Integer.parseInt(sdId[i]));
            }
        }

        return "删除成功!";
    }
}
