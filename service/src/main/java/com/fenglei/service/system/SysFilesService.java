package com.fenglei.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.model.system.entity.SysFiles;
import org.apache.poi.xssf.usermodel.XSSFPictureData;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface SysFilesService extends IService<SysFiles> {
    SysFiles addAndReturn(@RequestParam MultipartFile file, HttpServletRequest request) throws Exception;

    SysFiles addAndReturn(XSSFPictureData pictureData) throws Exception;

    SysFiles addAndReturn(String base64, String batchId) throws Exception;

    SysFiles addAndReturnUnSave(@RequestParam MultipartFile file, HttpServletRequest request) throws Exception;

}
