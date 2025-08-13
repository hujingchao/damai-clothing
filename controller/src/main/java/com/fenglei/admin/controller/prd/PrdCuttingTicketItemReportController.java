package com.fenglei.admin.controller.prd;

import com.fenglei.common.result.Result;
import com.fenglei.model.prd.entity.PrdCuttingTicketItemReport;
import com.fenglei.model.prd.vo.CuttingRouteDetailVo;
import com.fenglei.service.prd.IPrdCuttingTicketItemReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 裁床单 - 工票信息明细汇报 前端控制器
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-02
 */
@Api(tags = "裁床单 - 工票信息明细汇报 前端控制器")
@RestController
@RequestMapping("/api.admin/v1/prdCuttingTicketItemReport")
@AllArgsConstructor
@Slf4j
public class PrdCuttingTicketItemReportController {
    IPrdCuttingTicketItemReportService prdCuttingTicketItemReportService;

    @ApiOperation(value = "裁床单 - 工票信息明细汇报新增")
    @PostMapping("/add")
    public Result<PrdCuttingTicketItemReport> add(@RequestBody PrdCuttingTicketItemReport prdCuttingTicketItemReport) {
        PrdCuttingTicketItemReport result = prdCuttingTicketItemReportService.add(prdCuttingTicketItemReport);
        return Result.success(result);
    }

    @ApiOperation(value = "裁床单 - 工票信息明细汇报删除")
    @DeleteMapping("/removeById/{id}")
    public Result<Boolean> removeById(@PathVariable String id) {
        return Result.judge(prdCuttingTicketItemReportService.myRemoveById(id));
    }

    @ApiOperation(value = "裁床单 - 工票信息明细汇报批量删除")
    @DeleteMapping("/removeByIds")
    public Result<Boolean> removeByIds(@RequestBody PrdCuttingTicketItemReport prdCuttingTicketItemReport) {
        return Result.judge(prdCuttingTicketItemReportService.myRemoveByIds(prdCuttingTicketItemReport.getIds()));
    }

    @ApiOperation(value = "裁床单 - 工票信息明细汇报修改")
    @PutMapping("/update")
    public Result<Boolean> update(@RequestBody PrdCuttingTicketItemReport prdCuttingTicketItemReport) {
        return Result.judge(prdCuttingTicketItemReportService.myUpdate(prdCuttingTicketItemReport));
    }

    @ApiOperation(value = "裁床单 - 工票信息明细汇报详细信息")
    @GetMapping("/detail/{id}")
    public Result<PrdCuttingTicketItemReport> detail(@PathVariable String id) {
        PrdCuttingTicketItemReport result = prdCuttingTicketItemReportService.detail(id);
        return Result.success(result);
    }
}
