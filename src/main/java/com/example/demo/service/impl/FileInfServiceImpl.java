package com.example.demo.service.impl;

import com.example.demo.config.Const;
import com.example.demo.dao.*;
import com.example.demo.entity.*;
import com.example.demo.service.FileInfServive;
import com.example.demo.service.UserInfService;
import com.example.demo.util.DateUtil;
import com.example.demo.util.PreviewUtil;
import com.example.demo.util.ResponseUtil;
import com.example.demo.util.ZipCompressUtil;
import com.example.demo.vo.FileVO;
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
import java.util.ArrayList;
import java.util.List;

@Service
public class FileInfServiceImpl implements FileInfServive {

    @Autowired
    FileInfDao fileInfDao;
    @Autowired
    DirInfDao dirInfDao;
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
    @Autowired
    UserInfDao userInfDao;

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


    /*
    * 在一个文件夹下执行模糊搜索
    * */
    @Override
    public List<FileInf> selectListByFileNameAndDirId(String fileName, Integer dirId) {
        List<FileInf> fileInf = fileInfDao.selectListByFileNameAndDirId(fileName,dirId);
        return fileInf;
    }

    @Override
    public List<FileVO> selectFileVOListByDirId(Integer dirId) {
        List<FileVO> fileVOS = fileInfDao.selectFileVOListByDirId(dirId);
        return fileVOS;
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
        DirInf dirInf = dirInfDao.selectDirByFileId(fileId);//文件所在文件夹

        List<DirInf> originAccessPath = null;     //访问路径
        List<DirInf> accessPath = new ArrayList<>();     //访问路径
        //没排序的导航路径
        originAccessPath = dirInfDao.selectParentDirByDirId(dirInf.getDirId());
        //导航路径排序
        Integer item = dirInf.getDirId();
        for (int i = 0; i < originAccessPath.size(); i++) {
            for (DirInf inf : originAccessPath) {
                if(inf.getDirId().equals(item)){
                    accessPath.add(0,inf);
                    item = inf.getParentDir();
                }
            }
        }


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
        //更新文件柜空间
        FileCabinet fileCabinet = fileCabinetDao.selectByDirId(accessPath.get(0).getDirId());//根据文件夹id查文件柜
        BigInteger count = fileCabinet.getUsedSpace().subtract(fileInf.getFileSize());
        fileCabinet.setUsedSpace(count);
        fileCabinetDao.updateByPrimaryKeySelective(fileCabinet);

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
        DirInf dirInf = dirInfDao.selectByPrimaryKey(dirId); //获取目标文件夹
        DeptMember deptMember=null;
        List<DirInf> dirInfList = dirInfDao.selectParentDirByDirId(dirId);
        FileCabinet fileCabinet = fileCabinetDao.selectByDirId(dirInfList.get(0).getDirId());

        //判断空间是否已满了
        if(dirInf.getUserId() == userInf.getUserId()) {
            if (fileCabinet.getUsedSpace().add(new BigInteger(Long.valueOf(file.getSize()).toString())).compareTo(fileCabinet.getMaxSpace()) > 0) {
                return "空间已达上限";
            }
        }else{
            deptMember = deptMemberDao.selectByUserKey(userInf.getUserId());
            System.out.println(deptMember);
            if(deptMember.getUsedSpace().add(new BigInteger(Long.valueOf(file.getSize()).toString())).compareTo(deptMember.getMaxSpace())>0){
                return "空间已达上限";
            }
        }
        String systemPath = "D:\\graduation project\\ofms"; //系统路径
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
        //判断数据库中是否存在该文件的记录
        boolean flag = true;    //插入数据开关
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
        return "上传成功!";

    }

    /*
    文件下载
    */
    @Override
    public void download(Integer fileId, Integer dirId, HttpServletResponse response, HttpSession session) {
        String systemPath = "D:\\graduation project\\ofms";  //文件保存系统路径
        DirInf dirInf = dirInfDao.selectByPrimaryKey(dirId); //文件夹信息
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
                /*源文件夹通过ZipOutputStream加工获得文件夹的Zip压缩文件存放在临时文件夹，再通过缓存输入流把文件读出来
                最后通过ServletOutputStream响应给页面 */
            //压缩文件，存放到临时文件夹
            String temp = "D:\\graduation project\\ofms\\Tmpe";  //临时文件夹路径
            String zipFileName = temp + "\\" + dirInf.getDirName() + ".zip";      //输出的zip文件 路径 和 名称
            ZipCompressUtil zipCom = new ZipCompressUtil(zipFileName, systemPath + dirInf.getDirPath() + dirInf.getDirName());
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


    /*
    * 文件预览
    *
    * */
    @Override
    public String preview(Integer fileId, HttpServletResponse response) {
        String systemPath = "D:\\graduation project\\ofms"; //文件保存系统路径
        FileInf fileInf = fileInfServive.selectByPrimaryKey(fileId);//文件信息
        DirInf dirInf = dirInfDao.selectByPrimaryKey(fileInf.getDirId());//文件夹
        String fileRealPath = systemPath+dirInf.getDirPath()+ dirInf.getDirName()+ "\\" +fileInf.getFileName(); //预览路径
        String fileName = fileInf.getFileName();  //获取文件名
        //返回视频流或音频流，再浏览器中打开
        if(PreviewUtil.isVideoOrAudio(fileInf.getFileName())) {
            String file = fileRealPath;
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(file);
                byte[] data = new byte[inputStream.available()];
                inputStream.read(data);
                String diskfilename = "final.mp4";
                response.setContentType("video/mp4");
                response.setHeader("Content-Disposition", "inline; filename=\"" + diskfilename + "\""); //设置处理方式
                System.out.println("data.length " + data.length);
                response.setContentLength(data.length);
                response.setHeader("Content-Range", "" + Integer.valueOf(data.length - 1));
                response.setHeader("Accept-Ranges", "bytes");
                response.setHeader("Etag", "W/\"9767057-1323779115364\"");
                OutputStream os = response.getOutputStream();
                os.write(data);
                //先声明的流后关掉！
                os.flush();
                os.close();
                inputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(PreviewUtil.isDocument(fileInf.getFileName())) {
            //判断文件的类型是否是excel表格，如果是excel表格，统一将其缩放再转换
            if (fileName.indexOf(".xlsx") >= 0) {
                Workbook workbook = new Workbook();     //工作簿对象
                workbook.loadFromFile(fileRealPath);     //加载文件
                Worksheet worksheet = workbook.getWorksheets().get(0);  //
                worksheet.getPageSetup().setZoom(50);  //缩放50%
                workbook.saveToFile("D:\\test\\Temp Excel\\Temp.xlsx");     //保存到临时文件夹
                fileRealPath = "D:\\test\\Temp Excel\\Temp.xlsx";    //改变预览路径
            }

            File file = new File(fileRealPath); //要预览的目标文件
            File newFile = new File("D:\\test\\obj-pdf"); //转成PDF后存放的位置

            if(fileName.indexOf(".pdf") >= 0){
                try {
                    InputStream inputStream = new FileInputStream(file); //输入流读取文件
                    ServletOutputStream outputStream = response.getOutputStream(); //获得请求响应的输出流; //获得请求响应的输出流
                    //copy
                    int i = IOUtils.copy(inputStream, outputStream);      //读取并输出
                    inputStream.close();
                    outputStream.close();
                    outputStream = response.getOutputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            //如果文件夹不存在就创建
            if (!newFile.exists()) {
                newFile.mkdirs();
            }
            File pdfFile = new File("D:\\test\\obj-pdf\\hello.pdf");     //保存预览信息的pdf
            try {
                converter.convert(file).to(pdfFile).execute();      //转化成pdf保存到hello.pdf
                InputStream inputStream = new FileInputStream(pdfFile); //输入流读取文件
                ServletOutputStream outputStream = response.getOutputStream(); //获得请求响应的输出流
                //copy
                int i = IOUtils.copy(inputStream, outputStream);      //读取并输出
                //System.out.println(i);
                inputStream.close();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "preview_error";
    }

    /*
    * 移动文件
    * */
    @Override
    public String updateByPrimaryKeySelective(Integer fileId, Integer parentId) {
        //获取文件
        FileInf fileInf = fileInfDao.selectByPrimaryKey(fileId); //原文件
        DirInf dirInf = dirInfDao.selectByPrimaryKey(fileInf.getDirId()); //原文件夹
        DirInf tagetDir = dirInfDao.selectByPrimaryKey(parentId);   //目标文件夹
        String filePath = Const.SYSTEM_PATH + dirInf.getDirPath() + dirInf.getDirName()+"\\"+fileInf.getFileName(); //原文件路径
        String newFilePath = Const.SYSTEM_PATH + tagetDir.getDirPath() + tagetDir.getDirName()+"\\"+fileInf.getFileName(); //新文件路径
        //判断是否存在重复文件
        FileInf repeatFile =  fileInfDao.selectByFileNameAndDirId(fileInf.getFileName(),tagetDir.getDirId());
        if(repeatFile != null){
            return "文件已存在，操作失败！";
        }
        File file = new File(filePath);
        file.renameTo(new File(newFilePath));
        System.out.println(filePath);
        System.out.println(newFilePath);
        FileInf newfile = new FileInf();
        newfile.setFileId(fileId);
        newfile.setDirId(parentId);
        if(fileInfDao.updateByPrimaryKeySelective(newfile) == 0) return "操作失败";
        return "保存成功";
    }


    /*
    * 修改文件名
    * */
    @Override
    public int updateFileName(Integer fileId,String fileName) {
        String systemPath = "D:\\graduation project\\ofms";
        //修改硬盘文件名
        FileInf fileInf1 = fileInfDao.selectByPrimaryKey(fileId); //原文件
        DirInf dirInf = dirInfDao.selectByPrimaryKey(fileInf1.getDirId()); //原文件所在文件夹
        String dirPhat = systemPath+dirInf.getDirPath()+dirInf.getDirName()+"\\";
        File file = new File(dirPhat+fileInf1.getFileName()); //原文件文件类
        File newFile = new File(dirPhat+fileName);
        //校验新文件名是否在硬盘中是否存在，存在则返回对应信息
        if(newFile.exists()){
            System.out.println("文件已存在");
            return 2;       //文件已存在
        }
        //改名
        if(!file.renameTo(new File(systemPath+dirInf.getDirPath()+dirInf.getDirName()+"\\"+fileName))){
            return 0;
        }
        //数据库修改文件名
        FileInf fileInf = new FileInf();
        fileInf.setFileId(fileId);
        fileInf.setFileName(fileName);
        return fileInfDao.updateByPrimaryKeySelective(fileInf);   //修改
    }

    @Override
    public List<FileInf> selectByFileType(List<String> type,Integer userId,Integer dirId) {
        //从根目录开始搜索所有文件夹
        List<DirInf> dirInfList = dirInfDao.selectChildrenDirByDirId(dirId);
        //遍历文件夹，搜索其中的文件
        List<FileInf> fileInfList = new ArrayList<FileInf>();
        //for
        for (DirInf dirInf : dirInfList) {
            fileInfList.addAll(fileInfDao.selectByFileType(type,userId,dirInf.getDirId()));
        }
        return fileInfList;
    }

}
