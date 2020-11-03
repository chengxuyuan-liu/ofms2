package com.example.demo.controller;

import com.example.demo.entity.DeptInf;
import com.example.demo.entity.DirInf;
import com.example.demo.entity.FileInf;
import com.example.demo.entity.UserInf;
import com.example.demo.service.DirInfService;
import com.example.demo.service.FileInfServive;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class ViewController {
    @Autowired
    FileInfServive fileInfServive;
    @Autowired
    DirInfService dirInfService;

    /**
     * 主页
    **/
    @RequestMapping("/main")
    public String mainView(Integer dirId, Map<String,Object> map, HttpSession session){
        //调用业务
        List<DirInf> dirInfList;
        List<FileInf> fileInfList;
        List<DirInf> accessPath = null;
        //如果dirId为空
        if(dirId != null)
        {
            //获得文件id为dirId文件夹下的文件夹、文件
            dirInfList = dirInfService.selectDirListByDirId(dirId);
            fileInfList = fileInfServive.selectFileListByFolderId(dirId);
            //获得父文件夹
//            DirInf fatherFolder = dirInfService.selectFatherFolderById(
//                    dirInfService.selectFolderById(folderId).getParentFolder());
//            model.addAttribute("fatherFolder",fatherFolder);

            //导航路径
            accessPath = dirInfService.selectParentDirByDirId(dirId);

        }
        else
        {



            UserInf userInf = (UserInf) session.getAttribute("USER_SESSION");
            if(userInf == null)
            {
                return "login";
            }

            //获得根文件夹
            DirInf rootDir= dirInfService.selectRootDirByUserId(userInf.getUserId());
            //获得根文件夹下的文件夹
            dirInfList = dirInfService.selectDirListByDirId(rootDir.getDirId());
            //获得根文件夹下的文件
            fileInfList = fileInfServive.selectFileListByFolderId(rootDir.getDirId());
            //导航路径
            accessPath = dirInfService.selectParentDirByDirId(rootDir.getDirId());


        }

        //返回
        map.put("dirId",dirId);
        map.put("fileList",fileInfList);
        map.put("dirList",dirInfList);
        map.put("accessPath",accessPath);
        return "persionnal";

    }
}
