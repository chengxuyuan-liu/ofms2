package com.example.demo.service.impl;
import com.example.demo.dao.CollectDao;
import com.example.demo.dao.CollectionDetailDao;
import com.example.demo.dao.DeptMemberDao;
import com.example.demo.entity.Collect;
import com.example.demo.entity.CollectionDetail;
import com.example.demo.entity.DeptMember;
import com.example.demo.service.CollectService;
import com.example.demo.vo.CollectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Service
public class CollectServiceImpl implements CollectService {

    @Autowired
    CollectDao collectDao;
    @Autowired
    DeptMemberDao deptMemberDao;
    @Autowired
    CollectionDetailDao collectionDetailDao;

    /*
    * 显示记录
    * */
    @Override
    public List<CollectVO> all(Integer userId){
        DeptMember deptMember =  deptMemberDao.selectByUserKey(userId);
        if(deptMember!=null) {
            List<CollectVO> collectVOS = collectDao.selectAll(deptMember.getId());
            Date date = new Date();
            for (CollectVO collectVO : collectVOS) {
               CollectionDetail collectionDetail =  collectionDetailDao.selectByPrimaryKey(collectVO.getCdId());
               if(collectionDetail.getDeadline().before(date)) {
                   CollectionDetail collectionDetail1 = new CollectionDetail();
                   collectionDetail1.setCdStatus(0);;
                   collectionDetail1.setCdId(collectionDetail.getCdId());
                   collectionDetailDao.updateByPrimaryKeySelective(collectionDetail1);
               }
            }
            collectVOS = collectDao.selectAll(deptMember.getId());
            return collectVOS;
        }
        return null;
    }

    /*
     * 上传时收集表动作
     * */
    @Override
    public String upload(String fileName, Integer userId,Integer cdId){
        DeptMember deptMember = deptMemberDao.selectByUserKey(userId);

        //改变搜索详情表状态（cdId , uploadNum）
        List<Collect> collects =  collectDao.selectBycdId(cdId); //搜集情况
        CollectionDetail collectionDetail = collectionDetailDao.selectByPrimaryKey(cdId);//搜集详情
        for (Collect collect : collects) {
            if(collect.getMemberId().equals(deptMember.getId()) && collect.getsStatus().equals(1)){
                CollectionDetail collectionDetail1 = new CollectionDetail();
                collectionDetail1.setUploadNum(collectionDetail.getUploadNum()+1);
                collectionDetail1.setCdId(collectionDetail.getCdId());
                collectionDetailDao.updateByPrimaryKeySelective(collectionDetail1);
            }
        }
        //修改收集表
        if(collectDao.updateByMemberIdAanCdId(fileName,deptMember.getId(),cdId)==0) return "上传失败！";
        return "上传成功！";
    }
    
}

