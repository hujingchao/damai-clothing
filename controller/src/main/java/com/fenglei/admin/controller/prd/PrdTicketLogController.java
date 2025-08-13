package com.fenglei.admin.controller.prd;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.result.Result;
import com.fenglei.model.prd.entity.PrdTicketLog;
import com.fenglei.service.prd.IPrdTicketLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhaojunnan
 * @since 2024-05-06
 */
@Api(tags = " 前端控制器")
@RestController
@RequestMapping("/api.admin/v1/prdTicketLog")
@AllArgsConstructor
@Slf4j
public class PrdTicketLogController {
    IPrdTicketLogService prdTicketLogService;

    @ApiOperation(value = "新增")
    @PostMapping("/add")
    public Result<PrdTicketLog> add(@RequestBody PrdTicketLog prdTicketLog) {
        PrdTicketLog result = prdTicketLogService.add(prdTicketLog);
        return Result.success(result);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("/removeById/{id}")
    public Result<Boolean> removeById(@PathVariable String id) {
        return Result.judge(prdTicketLogService.myRemoveById(id));
    }

    @ApiOperation(value = "批量删除")
    @DeleteMapping("/removeByIds")
    public Result<Boolean> removeByIds(@RequestBody PrdTicketLog prdTicketLog) {
        return Result.judge(prdTicketLogService.myRemoveByIds(prdTicketLog.getIds()));
    }

    @ApiOperation(value = "修改")
    @PutMapping("/update")
    public Result<Boolean> update(@RequestBody PrdTicketLog prdTicketLog) {
        return Result.judge(prdTicketLogService.myUpdate(prdTicketLog));
    }

    @ApiOperation(value = "分页查询")
    @GetMapping("/page")
    public Result<IPage<PrdTicketLog>> page(Page page, PrdTicketLog prdTicketLog) {
        IPage<PrdTicketLog> result = prdTicketLogService.myPage(page, prdTicketLog);
        return Result.success(result);
    }

    @ApiOperation(value = "详细信息")
    @GetMapping("/detail/{id}")
    public Result<PrdTicketLog> detail(@PathVariable String id) {
        PrdTicketLog result = prdTicketLogService.detail(id);
        return Result.success(result);
    }
}
