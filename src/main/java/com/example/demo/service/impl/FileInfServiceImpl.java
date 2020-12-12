package com.example.demo.service.impl;

import com.example.demo.dao.DeptMemberDao;
import com.example.demo.dao.FileCabinetDao;
import com.example.demo.dao.FileInfDao;
import com.example.demo.entity.*;
import com.example.demo.service.DeptMemberService;
import com.example.demo.service.DirInfService;
import com.example.demo.service.FileInfServive;
import com.example.demo.service.UserInfService;
import com.example.demo.util.DateUtil;
import com.example.demo.util.PreviewUtil;
import com.example.demo.util.ResponseUtil;
import com.example.demo.util.ZipCompress;
import com.spire.xls.Workbook;
import com.spire.xls.Worksheet;
import org.apache.commons.io.IOUtils;
import org.jodconverter.DocumentConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.math.BigInteger;
import java.util.List;

@Service
public class FileInfServiceImpl implements FileInfServive {

    @Autowired
    FileInfDao fileInfDao;
    @Autowired
    DirInfService dirInfService;
    @Autowired
    FileInfServive fileInfServive;
    @Autowired
    private DocumentConverter converter;
    @Autowired
    UserInfService userInfService;

    @Autowired
    DeptMemberDao deptMemberDao;
    @Autowired
    FileCabinetDao fileCabinetDao;

    /*
    查询
    */
    @Override
    public List<FileInf> selectFileListByFolderId(Integer dirId) {
        List<FileInf> result = fileInfDao.selectFileListByFolderId(dirId);
        return result;
    }

    @Override
    public FileInf selectByPrimaryKey(Integer fileId) {
        FileInf fileInf = fileInfDao.selectByPrimaryKey(fileId);
        return fileInf;
    }

    @Override
    public FileInf selectByFileNameAndDirId(String fileName, Integer dirId) {
        FileInf fileInf = fileInfDao.selectByFileNameAndDirId(fileName,dirId);
        return fileInf;
    }

    /*
    删除
    */
    @Override
    public int deleteByDirId(Integer dirId) {

        int result = fileInfDao.deleteByDirId(dirId);
        return result;
    }

    @Override
    public int deleteByUserId(Integer userId) {
        int result = fileInfDao.deleteByUserId(userId);
        return result;
    }

    @Override
    public Boolean deleteByPrimaryKey(Integer fileId) {

        //外存删除相应文件
        //获得获得文件的存储路径
        String systemPath = "D:\\graduation project\\ofms\\";
        DirInf dirInf = dirInfService.selectDirByFileId(fileId);
        FileInf fileInf = fileInfDao.selectByPrimaryKey(fileId);
        String filePath = systemPath+ dirInf.getDirPath()+dirInf.getDirName();

        //创建文件类
        File file = new File(filePath,fileInf.getFileName());

        //删除文件
        if (file.exists()) {
            file.delete();
        }


        //数据库删除记录
        int result = fileInfDao.deleteByPrimaryKey(fileId);
        if (result != 0) return true;
        return false;
    }

    @Override
    public List<FileInf> selectByFileName(String fileName, Integer fileId) {
        List<FileInf> fileInf = fileInfDao.selectByFileName(fileName,fileId);
        return fileInf;
    }

    /*
    文件上传
    */
    @Override
    public String fileUpload(MultipartFile file, Integer dirId, UserInf userInf) {

        DirInf dirInf = dirInfService.selectByPrimaryKey(dirId); //获取目标文件夹
        DeptMember deptMember=null;
        List<DirInf> dirInfList = dirInfService.selectParentDirByDirId(dirId);
        FileCabinet fileCabinet = fileCabinetDao.selectByDirId(dirInfList.get(0).getDirId());


        //判断空间是否已满了
        if(dirInf.getUserId() == userInf.getUserId()) {
            if (fileCabinet.getUsedSpace().add(new BigInteger(Long.valueOf(file.getSize()).toString())).compareTo(fileCabinet.getMaxSpace()) > 0) {
                return "full";
            }


        }else{
            deptMember = deptMemberDao.selectByUserKey(userInf.getUserId());
            System.out.println(deptMember);
            if(deptMember.getUsedSpace().add(new BigInteger(Long.valueOf(file.getSize()).toString())).compareTo(deptMember.getMaxSpace())>0){
                return "full";
            }
        }



        //系统路径
        String systemPath = "D:\\graduation project\\ofms";
        //获取上传文件的信息
        String fileOriginalName = file.getOriginalFilename();  //文件的原始名
        String fileName = fileOriginalName;
        String fileType = fileOriginalName.substring(fileOriginalName.lastIndexOf(".") + 1); //获取文件类型
        Integer fileSize =  (int)file.getSize(); //获取文件的大小，单位为字节



        FileInf repeatFile = fileInfDao.selectByFileNameAndDirId(fileName,dirId);

        FileInf fileInf = new FileInf();
        fileInf.setFileName(fileName);//设置文件名
        fileInf.setFileSize(new BigInteger(fileSize.toString()));//设置文件大小，以字节为单位
        fileInf.setFileType(fileType);//设置文件的类型
        fileInf.setFileUploadTime(DateUtil.getNowDate());//设置上传时间
        fileInf.setDirId(dirId);//文件夹id
        fileInf.setUserId(userInf.getUserId());//用户id



        boolean flag = true;      //插入数据开关
        //判断数据库中是否存在该文件的记录
        if (repeatFile != null) {
            //如果文件字节相同，则覆盖，用户空间不用更新
            //如果文件字节大小不同，则更改名字后插入数据库，并且更新用户空间
            int fix = 0;

            String perfix = fileName.substring(0,fileName.lastIndexOf("."));
            String suffix = fileName.substring(fileName.lastIndexOf("."));
            do{
                if(repeatFile.getFileSize().equals(fileInf.getFileSize())){
                    flag = false;
                    break;
                }
                fix+=1;
                fileName = perfix+"("+(fix)+")"+suffix;
                System.out.println("文件名="+fileName);
                repeatFile = fileInfDao.selectByFileNameAndDirId(fileName,dirId);
            }while (repeatFile != null);

        }

        //判断是否要插入文件信息
        if(flag) {
            //插入文件信息
            fileInf.setFileName(fileName);
            if (fileInfDao.insertSelective(fileInf) == 0) return "inset false";
            //更新已使用空间
            int count;
            if(dirInf.getUserId() != userInf.getUserId()){
                //部门成员更新空间
                BigInteger n = deptMember.getUsedSpace().add(new BigInteger(fileInf.getFileSize().toString()));
                System.out.println("使用空间："+n);
                deptMember.setUsedSpace(n);
                deptMember.setRecent(DateUtil.getNowDate());
                deptMemberDao.updateByPrimaryKeySelective(deptMember);
            }else {
                //用户更新空间
                BigInteger updateSpcae = fileCabinet.getUsedSpace().add(new BigInteger(Long.valueOf(file.getSize()).toString()));
                fileCabinet.setUsedSpace(updateSpcae);
                //当前文件柜的used_space增加
                fileCabinetDao.updateByPrimaryKeySelective(fileCabinet);
            }

        }

        //上传文件到外存
        //判断文件是否存在
        if (!file.isEmpty() && file.getSize() > 0) {
            //该文件的原始名
            //获得保存的路径
            String realPath = systemPath+dirInf.getDirPath()+"\\"+dirInf.getDirName();
            System.out.println(realPath);
            //使用UUID重新命名上传的文件,上传人_UUID_原始文件名；
            //上传文件
            try {
                file.transferTo(new File(realPath, fileName));
            } catch (IOException e) {
                e.printStackTrace();
                return "save false";
            }
        }
        return "successful";

    }

    /*
    文件下载
    */
    @Override
    public void download(Integer fileId, Integer dirId, HttpServletResponse response, HttpSession session) {

        String systemPath = "D:\\graduation project\\ofms";  //文件保存系统路径
        DirInf dirInf = dirInfService.selectByPrimaryKey(dirId); //文件夹信息
        UserInf userInf = (UserInf) session.getAttribute("USER_SESSION"); //用户信息

        //输入流
        byte[] buffer = new byte[1024];     //缓存区
        FileInputStream fis = null;         //文件输入流
        BufferedInputStream bis = null;     //缓存输入流


        //判断是文件还是文件夹
        if (fileId != null) {   //如果是文件
            FileInf fileInf = fileInfServive.selectByPrimaryKey(fileId);
            System.out.println(fileInf);
            String filePaht = systemPath + dirInf.getDirPath() + dirInf.getDirName() + "\\" + fileInf.getFileName();     //文件所在路径

            //返回文件给浏览器
            try {
                ResponseUtil.responFileToBrower(response, filePaht);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
                /*思路：源文件夹通过ZipOutputStream加工获得文件夹的Zip压缩文件存放在临时文件夹，再通过缓存输入流把文件读出来
                最后通过ServletOutputStream响应给页面 */

            //压缩文件，存放到临时文件夹
            String temp = "D:\\graduation project\\ofms\\Tmpe";  //临时文件夹路径
            String zipFileName = temp + "\\" + dirInf.getDirName() + ".zip";      //输出的zip文件 路径 和 名称
            ZipCompress zipCom = new ZipCompress(zipFileName, systemPath + dirInf.getDirPath() + dirInf.getDirName());
            try {
                zipCom.zip();
            } catch (Exception e) {
                e.printStackTrace();
            }

            //返回文件给浏览器
            try {
                ResponseUtil.responFileToBrower(response, zipFileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String preview(Integer fileId, HttpServletResponse response) {
        String systemPath = "D:\\graduation project\\ofms"; //文件保存系统路径
        FileInf fileInf = fileInfServive.selectByPrimaryKey(fileId);//文件信息
        DirInf dirInf = dirInfService.selectByPrimaryKey(fileInf.getDirId());//文件夹
        //判断该文件是否可预览
        if(!PreviewUtil.check(fileInf.getFileName())) return "preview_error";
        //获取绝对路径
        String fileRealPath = systemPath+dirInf.getDirPath()+ dirInf.getDirName()+ "\\" +fileInf.getFileName();
        //获取文件名
        String fileName = fileInf.getFileName();
        //文件类型
        //判断文件的类型是否是excel表格
        if(fileName.indexOf(".xlsx") >= 0) {
            //如果是excel表格，统一将其缩放再转换
            Workbook workbook = new Workbook();
            workbook.loadFromFile(fileRealPath);
            Worksheet worksheet = workbook.getWorksheets().get(0);  //
            worksheet.getPageSetup().setZoom(50);
            workbook.saveToFile("D:\\test\\Temp Excel\\Temp.xlsx");
            fileRealPath = "D:\\test\\Temp Excel\\Temp.xlsx";
        }
        //要预览的文件
        File file = new File(fileRealPath);
        //转成PDF后存放的位置
        File newFile = new File("D:\\test\\obj-pdf");
        if (!newFile.exists()) {
            newFile.mkdirs();
        }
        //pdf文件
        File pdfFile = new File("D:\\test\\obj-pdf\\hello.pdf");
        try{
            //开始文件转换
            converter.convert(file).to(pdfFile).execute();
            //输入流读取文件
            InputStream inputStream = new FileInputStream(pdfFile);
            //获得前端的输出流
            ServletOutputStream outputStream = response.getOutputStream();
            //copy
            int i = IOUtils.copy(inputStream, outputStream);
            //System.out.println(i);
            inputStream.close();
            outputStream.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "OK";
    }

    @Override
    public int updateByPrimaryKeySelective(Integer fileId,Integer parentId) {
        FileInf fileInf = new FileInf();
        fileInf.setFileId(fileId);
        fileInf.setDirId(parentId);
        int result = fileInfDao.updateByPrimaryKeySelective(fileInf);
        return result;
    }


}
