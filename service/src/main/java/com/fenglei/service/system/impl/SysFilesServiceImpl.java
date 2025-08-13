package com.fenglei.service.system.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.common.util.*;
import com.fenglei.mapper.system.SysFilesMapper;
import com.fenglei.model.system.entity.SysFiles;
import com.fenglei.service.system.SysFilesService;
import org.apache.poi.xssf.usermodel.XSSFPictureData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Date;

/**
 * User: yzy
 * Date: 2019/11/4 0004
 * Time: 10:18
 * Description: No Description
 */
@Service
public class SysFilesServiceImpl extends ServiceImpl<SysFilesMapper, SysFiles> implements SysFilesService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized SysFiles addAndReturn(@RequestParam MultipartFile file, HttpServletRequest request) throws Exception {
        SysFiles sysFiles = new SysFiles();
        long size = file.getSize();
        sysFiles.setSize(String.valueOf(size));
        String originalFilename = file.getOriginalFilename();
        sysFiles.setFileName(FileUtil.getFileNameNoEx(originalFilename) + "." + FileUtil.getExtensionName(originalFilename));
        sysFiles.setFileType("." + FileUtil.getExtensionName(originalFilename));
        InputStream inputStream = file.getInputStream();

        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        LocalDate date = LocalDate.now();
        String objectKey = date.getYear() + "/" + date.getMonthValue() + "/" + date.getDayOfMonth() + "/" + IdUtil.simpleUUID() + "." + suffix;

        //如果是图片文件就进行压缩
        if (ImageUtil.isImage(file.getOriginalFilename())) {
            inputStream = ImageUtil.getInputStream(
                    ImageUtil.compress(ImageIO.read(inputStream)),
                    ImageUtil.getFileExtention(file.getOriginalFilename()));
            objectKey = "images/" + objectKey;
        } else {
            objectKey = "files/" + objectKey;
        }
        String url = MinioUtils.uploadInputStream(objectKey, inputStream);
        if (url == null) {
            return null;
        } else {
            sysFiles.setUrl(url);
            //默认当前时间上传
            if(StringUtils.isEmpty(sysFiles.getCreateTime())){
                SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                sysFiles.setCreateTime(dft.format(new Date()));
            }
            sysFiles.setCreatorId(RequestUtils.getUserId());
            sysFiles.setCreator(RequestUtils.getNickname());
            baseMapper.insert(sysFiles);
            return sysFiles;
        }
    }

    @Override
    public SysFiles addAndReturn(XSSFPictureData pictureData) throws Exception {
        String ext = pictureData.suggestFileExtension();//获取扩展名
        InputStream inputStream = new ByteArrayInputStream(pictureData.getData());
        LocalDate date = LocalDate.now();
        String objectKey = "files/" + date.getYear() + "/" + date.getMonthValue() + "/" + date.getDayOfMonth() + "/" + IdUtil.simpleUUID() + "." + ext;

        String url = MinioUtils.uploadInputStream(objectKey, inputStream);
        if (url == null) {
            return null;
        } else {
            SysFiles sysFiles = new SysFiles();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSSSSSSSS");
            String originalFilename = sdf.format(new Date());
            sysFiles.setFileName(FileUtil.getFileNameNoEx(originalFilename) + "." + FileUtil.getExtensionName(originalFilename));
            sysFiles.setFileType("." + FileUtil.getExtensionName(originalFilename));
            sysFiles.setUrl(url);
            baseMapper.insert(sysFiles);
            return sysFiles;
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysFiles addAndReturn(String base64, String batchId) throws Exception {
        String[] split = base64.split(",");
        // 获取后缀
        byte[] b = Base64.getDecoder().decode(split[1]);
        String ext = "";
        if (0x424D == ((b[0] & 0xff) << 8 | (b[1] & 0xff))) {
            ext = "bmp";
        } else if (0x8950 == ((b[0] & 0xff) << 8 | (b[1] & 0xff))) {
            ext = "png";
        } else if (0xFFD8 == ((b[0] & 0xff) << 8 | (b[1] & 0xff))) {
            ext = "jpg";
        }
        InputStream inputStream = new ByteArrayInputStream(b);
        LocalDate date = LocalDate.now();
        String name = IdUtil.simpleUUID();
        String objectKey = "images/signature/" + date.getYear() + "/" + date.getMonthValue() + "/" + date.getDayOfMonth() + "/" + name + "." + ext;
        String url = MinioUtils.uploadInputStream(objectKey, inputStream);
        if (url == null) {
            return null;
        }
        SysFiles sysFiles = new SysFiles();
        if (StringUtils.isEmpty(batchId)) {
            sysFiles.setBatchId(IdWorker.getIdStr());
        } else {
            sysFiles.setBatchId(batchId);
        }
        sysFiles.setFileName(name + "." + ext);
        sysFiles.setUrl(url);
        baseMapper.insert(sysFiles);
        return sysFiles;
    }

    @Override
    public SysFiles addAndReturnUnSave(MultipartFile file, HttpServletRequest request) throws Exception {
        SysFiles sysFiles = new SysFiles();
        long size = file.getSize();
        sysFiles.setSize(String.valueOf(size));
        String originalFilename = file.getOriginalFilename();
        sysFiles.setFileName(FileUtil.getFileNameNoEx(originalFilename) + "." + FileUtil.getExtensionName(originalFilename));
        sysFiles.setFileType("." + FileUtil.getExtensionName(originalFilename));
        InputStream inputStream = file.getInputStream();

        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        LocalDate date = LocalDate.now();
        String objectKey = date.getYear() + "/" + date.getMonthValue() + "/" + date.getDayOfMonth() + "/" + IdUtil.simpleUUID() + "." + suffix;

        //如果是图片文件就进行压缩
        if (ImageUtil.isImage(file.getOriginalFilename())) {
            inputStream = ImageUtil.getInputStream(
                    ImageUtil.compress(ImageIO.read(inputStream)),
                    ImageUtil.getFileExtention(file.getOriginalFilename()));
            objectKey = "images/" + objectKey;
        } else {
            objectKey = "files/" + objectKey;
        }
        String url = MinioUtils.uploadInputStream(objectKey, inputStream);
        if (url == null) {
            return null;
        } else {
            sysFiles.setUrl(url);
            return sysFiles;
        }
    }
}
