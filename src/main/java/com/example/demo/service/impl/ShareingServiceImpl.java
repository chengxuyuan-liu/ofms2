package com.example.demo.service.impl;

import com.example.demo.dao.DeptMemberDao;
import com.example.demo.dao.ShareDetailDao;
import com.example.demo.dao.ShareingDao;
import com.example.demo.entity.DeptMember;
import com.example.demo.entity.ShareDetail;
import com.example.demo.entity.Shareing;
import com.example.demo.entity.UserInf;
import com.example.demo.service.DeptMemberService;
import com.example.demo.service.ShareDetailService;
import com.example.demo.service.ShareingService;
import com.example.demo.vo.ShareingVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;


@Service
public class ShareingServiceImpl implements ShareingService {
    @Autowired
    ShareingDao shareingDao;
    @Autowired
    DeptMemberDao deptMemberDao;
    @Autowired
    ShareDetailDao shareDetailDao;
    /*
     * 显示所有记录
     * */
    @Override
    public List<ShareingVO> all(UserInf userInf) {
        //区分用户，团队账户和个人账户
        DeptMember deptMember = deptMemberDao.selectByUserKey(userInf.getUserId());
        List<ShareingVO> shareingVO = shareingDao.selectAll(deptMember.getId());
        return shareingVO;
    }

    /*
    * 修改下载人数
    * */
    @Override
    public void updateLoadNum(Integer sdId,Integer userId){
        ShareDetail shareDetail = shareDetailDao.selectByPrimaryKey(sdId);
        //获取共享信息
        List<Shareing> shareings =  shareingDao.selectBySdId(sdId);
        //获取成员信息
        DeptMember deptMember = deptMemberDao.selectByUserKey(userId);
        //
        for (Shareing shareing : shareings) {
            if(shareing.getMemberId().equals(deptMember.getId())){
                if(shareing.getsStatus().equals(1)){
                    Shareing updataShare = new Shareing();
                    updataShare.setsStatus(0);
                    updataShare.setShareId(shareing.getShareId());
                    //修改共享对象状态
                    shareingDao.updateByPrimaryKeySelective(updataShare);
                    //修改下载人数
                    shareDetail.setLoadNum(shareDetail.getLoadNum()+1);
                    shareDetailDao.updateByPrimaryKeySelective(shareDetail);
                }

            }
        }
    }




}
