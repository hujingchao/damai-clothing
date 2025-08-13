package com.fenglei.admin.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.result.Result;
import com.fenglei.model.quartz.entity.SysJob;
import com.fenglei.service.quartz.ISysJobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 调度任务信息操作处理
 *
 * @author ruoyi
 */
@Api(tags = "调度任务信息操作处理")
@RestController
@RequestMapping("/api.admin/v1/job")
@RequiredArgsConstructor
public class SysJobController {

    @Resource
    private ISysJobService sysJobService;

    @ApiOperation(value = "新增定时任务")
    @PostMapping("/add")
    public Result add(@RequestBody SysJob job) throws Exception {
        SysJob result = sysJobService.add(job);
        return Result.success(result);
    }

    @ApiOperation(value = "删除定时任务")
    @DeleteMapping("/removeById/{id}")
    public Result removeById(@PathVariable String id) throws Exception {
        return Result.judge(sysJobService.myRemoveById(id));
    }

    @ApiOperation(value = "批量删除定时任务")
    @DeleteMapping("/removeByIds")
    public Result removeByIds(@RequestBody SysJob job) throws Exception {
        return Result.judge(sysJobService.myRemoveByIds(job.getIds()));
    }

    @ApiOperation(value = "修改定时任务")
    @PutMapping("/update")
    public Result edit(@RequestBody SysJob job) throws Exception {
        SysJob result = sysJobService.myUpdate(job);
        return Result.success(result);
    }

    @ApiOperation(value = "分页查询")
    @GetMapping("/page")
    public Result page(Page page, SysJob sysJob) {
        IPage<SysJob> result = sysJobService.myPage(page, sysJob);
        return Result.success(result);
    }

    @ApiOperation(value = "详细信息")
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable String id) {
        SysJob result = sysJobService.detail(id);
        return Result.success(result);
    }

    /**
     * 定时任务状态修改
     */
    @PutMapping("/changeStatus")
    public Result changeStatus(@RequestBody SysJob job) throws Exception {
        SysJob result = sysJobService.changeStatus(job);
        return Result.success(result);
    }

    /**
     * 定时任务立即执行一次
     */
    @PutMapping("/run")
    public Result run(@RequestBody SysJob job) throws Exception {
        boolean result = sysJobService.run(job);
        return result ? Result.success() : Result.failed("任务不存在或已过期！");
    }
}
