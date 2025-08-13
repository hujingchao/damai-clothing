package com.fenglei.admin.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.result.Result;
import com.fenglei.model.system.entity.SysFiles;
import com.fenglei.service.system.SysFilesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * User: yzy
 * Date: 2019/11/4 0004
 * Time: 10:17
 * Description: No Description
 */

@Api(tags = "图片上传接口")
@RestController
@RequestMapping("/api.admin/v1/picture")
@Slf4j
public class PictureController {

    @Resource
    private SysFilesService sysFilesService;

    @ApiOperation(value = "图片列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "分页对象", required = true, paramType = "body", dataType = "Page"),
            @ApiImplicitParam(name = "picture", value = "图片对象", required = true, paramType = "body", dataType = "Picture")
    })
    @GetMapping("/tools/picture")
    public Result list(Page page, SysFiles sysFiles) {
        IPage<SysFiles> pictures = sysFilesService.page(page, new QueryWrapper<SysFiles>()
                .lambda()
                .orderByDesc(SysFiles::getCreateTime));
        return Result.success(pictures);
    }

    @ApiOperation(value = "一张图")
    @ApiImplicitParam(name = "picture", value = "分页对象", required = true, paramType = "body", dataType = "Picture")
    @GetMapping("/tools/picture/one")
    public Result get(SysFiles sysFiles) {
        SysFiles result = sysFilesService.getOne(
                new QueryWrapper<SysFiles>()
                        .lambda()
                        .like(SysFiles::getId, sysFiles.getId())
        );
        return Result.success(result);
    }

    @ApiOperation(value = "上传图片")
    @ApiImplicitParam(name = "file", value = "文件", paramType = "form", dataType = "__file")
    @PostMapping("/tools/pictures")
    public Result pictures(@RequestParam MultipartFile file, HttpServletRequest request) throws Exception {
        return Result.success(sysFilesService.addAndReturn(file, request));
    }

    @ApiOperation(value = "上传图片不保存")
    @ApiImplicitParam(name = "file", value = "文件", paramType = "form", dataType = "__file")
    @PostMapping("/tools/picturesUnSave")
    public Result picturesUnSave(@RequestParam MultipartFile file, HttpServletRequest request) throws Exception {
        return Result.success(sysFilesService.addAndReturnUnSave(file, request));
    }


    @RequestMapping("/tools/addPictures")
    public Result addPictures(@RequestParam MultipartFile file, HttpServletRequest request) throws Exception {
        return Result.success(sysFilesService.addAndReturn(file, request));
    }



    @ApiOperation(value = "删除图片")
    @ApiImplicitParam(name = "id", value = "图片ID", required = true, paramType = "path", dataType = "String")
    @PatchMapping("/tools/pictures/{id}")
    public Result hide(@PathVariable String id) {
        if (sysFilesService.removeById(id)) {
            return Result.success("删除成功");
        }
        return Result.failed("删除失败");
    }


    @GetMapping("/getAll")
    public Result getAll() {
        List<SysFiles> sysFilesList = sysFilesService.list(new LambdaQueryWrapper<SysFiles>());
        return Result.success(sysFilesList);
    }


    @RequestMapping("/tools/uploadVideo")
    public Result uploadVideo(@RequestParam MultipartFile file, HttpServletRequest request) throws Exception {
        String realPath = request.getSession().getServletContext().getRealPath("/uploadFile/");
        File dir = new File(realPath);
        if (!dir.isDirectory()) {//文件目录不存在，就创建一个
            dir.mkdirs();
        }
        try {
            String filename = file.getOriginalFilename();
            //服务端保存的文件对象
            File fileServer = new File(dir, filename);
            System.out.println("file文件真实路径:" + fileServer.getAbsolutePath());
            //2，实现上传
            file.transferTo(fileServer);
            String filePath = request.getScheme() + "://" +
                    request.getServerName() + ":"
                    + request.getServerPort()
                    + "/uploadFile/" + filename;
            //3，返回可供访问的网络路径
            return Result.success(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.failed("上传失败");
    }
}
