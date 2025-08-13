package com.fenglei.admin.controller.prd;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.annotation.SysLogAnnotation;
import com.fenglei.common.result.Result;
import com.fenglei.model.fin.dto.PieceRateFilter;
import com.fenglei.model.prd.dto.CuttingFilterDto;
import com.fenglei.model.prd.dto.DoProcedureReportDto;
import com.fenglei.model.prd.dto.MoAsSrcDto;
import com.fenglei.model.prd.dto.UpdateTicketItemDto;
import com.fenglei.model.prd.entity.PrdCutting;
import com.fenglei.model.prd.entity.PrdCuttingTicketItemReport;
import com.fenglei.model.prd.vo.*;
import com.fenglei.service.prd.IPrdCuttingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 裁床单 - 主体 前端控制器
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-02
 */
@Api(tags = "裁床单 - 主体 前端控制器")
@RestController
@RequestMapping("/api.admin/v1/prdCutting")
@AllArgsConstructor
@Slf4j
public class PrdCuttingController {
    IPrdCuttingService prdCuttingService;

    @ApiOperation(value = "裁床单 - 主体新增")
    @PostMapping("/add")
    public Result<PrdCutting> add(@RequestBody PrdCutting prdCutting) {
        PrdCutting result = prdCuttingService.add(prdCutting);
        return Result.success(result);
    }

    @ApiOperation(value = "裁床单 - 主体删除")
    @DeleteMapping("/removeById/{id}")
    public Result<Boolean> removeById(@PathVariable String id) throws Exception {
        return Result.judge(prdCuttingService.myRemoveById(id));
    }

    @ApiOperation(value = "裁床单 - 主体批量删除")
    @DeleteMapping("/removeByIds")
    public Result<Boolean> removeByIds(@RequestBody PrdCutting prdCutting) throws Exception {
        return Result.judge(prdCuttingService.myRemoveByIds(prdCutting.getIds()));
    }

    @ApiOperation(value = "裁床单 - 主体修改")
    @PutMapping("/update")
    public Result<Boolean> update(@RequestBody PrdCutting prdCutting) throws Exception {
        return Result.judge(prdCuttingService.myUpdate(prdCutting));
    }

    @ApiOperation(value = "裁床单 - 主体分页查询")
    @PostMapping("/page")
    public Result<IPage<PrdCutting>> page(Page<PrdCutting> page, @RequestBody CuttingFilterDto cuttingFilterDto) {
        IPage<PrdCutting> result = prdCuttingService.myPage(page, cuttingFilterDto);
        return Result.success(result);
    }

    @ApiOperation(value = "裁床单导出")
    @PostMapping("/exportPrdCutting")
    //todo 暂时不加
    public void exportPrdCutting(HttpServletResponse response, @RequestBody CuttingFilterDto cuttingFilterDto) throws Exception {
        prdCuttingService.exportPrdCutting(response, cuttingFilterDto);
    }


    @ApiOperation(value = "裁床单 - 主体查询汇总")
    @PostMapping("/listSum")
    public Result<JSONObject> listSum(@RequestBody CuttingFilterDto cuttingFilterDto) {
        JSONObject result = prdCuttingService.listSum(cuttingFilterDto);
        return Result.success(result);
    }

    @ApiOperation(value = "裁床单 - 主体详细信息")
    @GetMapping("/detail/{id}")
    public Result<PrdCutting> detail(@PathVariable String id) {
        PrdCutting result = prdCuttingService.detail(id);
        return Result.success(result);
    }

    @ApiOperation(value = "app工序上数列表接口")
    @GetMapping("/detailForProcedureReport/{id}")
    public Result<List<ProcedureReportVo>> detailForProcedureReport(@PathVariable("id") String id) {
        List<ProcedureReportVo> result = prdCuttingService.detailForProcedureReport(id);
        return Result.success(result);
    }

    @ApiOperation(value = "app工序详情")
    @GetMapping("/detailCuttingRouteById/{routeId}")
    public Result<ProcedureReportVo> detailCuttingRouteById(@PathVariable("routeId") String routeId) {
        ProcedureReportVo result = prdCuttingService.detailCuttingRouteById(routeId);
        return Result.success(result);
    }

    @ApiOperation(value = "app扎号上数列表接口")
    @GetMapping("/detailForTicketReport/{id}")
    public Result<List<CuttingTicketItemDetailVo>> detailForTicketReport(@PathVariable("id") String id) {
        List<CuttingTicketItemDetailVo> result = prdCuttingService.detailForTicketReport(id);
        return Result.success(result);
    }

    @ApiOperation(value = "app单工序上数接口")
    @PostMapping("/doProcedureReport")
    @SysLogAnnotation("单工序上数")
    public Result<Boolean> doProcedureReport(@RequestBody List<DoProcedureReportDto> doProcedureReportDtos) {
        return Result.judge(prdCuttingService.doProcedureReport(doProcedureReportDtos));
    }

    @ApiOperation(value = "app多工序上数接口")
    @PostMapping("/doBatchProcedureReport")
    @SysLogAnnotation("多工序上数")
    public Result<Boolean> doBatchProcedureReport(@RequestBody List<DoProcedureReportDto> doProcedureReportDtos) {
        return Result.judge(prdCuttingService.doBatchProcedureReport(doProcedureReportDtos));
    }

    @ApiOperation(value = "app工序撤数接口")
    @PostMapping("/cancelReport")
    @SysLogAnnotation("工序撤数")
    public Result<Boolean> cancelReport(@RequestBody List<String> cuttingTicketItemReportIds) {
        return Result.judge(prdCuttingService.cancelReport(cuttingTicketItemReportIds));
    }

    @ApiOperation(value = "改菲接口")
    @PostMapping("/updateTicketItemQty")
    @SysLogAnnotation("改菲")
    public Result<Boolean> updateTicketItemQty(@RequestBody UpdateTicketItemDto updateTicketItemDto) {
        return Result.judge(prdCuttingService.updateTicketItemQty(updateTicketItemDto));
    }

    @ApiOperation(value = "批量改菲接口")
    @PostMapping("/batchUpdateTicketItemQty")
    @SysLogAnnotation("批量改菲")
    public Result<Boolean> batchUpdateTicketItemQty(@RequestBody List<UpdateTicketItemDto> updateTicketItemDtos) {
        return Result.judge(prdCuttingService.batchUpdateTicketItemQty(updateTicketItemDtos));
    }

    @ApiOperation(value = "app重新上数接口")
    @PostMapping("/updateReport")
    @SysLogAnnotation("重新上数")
    public Result<Boolean> updateReport(@RequestBody PrdCuttingTicketItemReport cuttingTicketItemReport) {
        return Result.judge(prdCuttingService.updateReport(cuttingTicketItemReport));
    }

    @ApiOperation(value = "根据扎号id获取扎号详情列表")
    @GetMapping("/getTicketDetailByTicketItemId/{ticketItemId}")
    public Result<CuttingTicketItemDetailVo> getTicketDetailByTicketItemId(@PathVariable String ticketItemId) {
        CuttingTicketItemDetailVo result = prdCuttingService.getTicketDetailByTicketItemId(ticketItemId);
        return Result.success(result);
    }

    @ApiOperation(value = "根据扎号id获取扎号详情列表")
    @GetMapping("/getTicketDetailByTicketNo/{ticketNo}")
    public Result<CuttingTicketItemDetailVo> getTicketDetailByTicketNo(@PathVariable String ticketNo) {
        CuttingTicketItemDetailVo result = prdCuttingService.getTicketDetailByTicketNo(ticketNo);
        return Result.success(result);
    }

    @ApiOperation(value = "根据裁床单id获取工票信息明细详细信息")
    @GetMapping("/listTicketItemDetailByCuttingId/{cuttingId}")
    public Result<List<CuttingTicketItemDetailVo>> listTicketItemDetailByCuttingId(@PathVariable String cuttingId) {
        List<CuttingTicketItemDetailVo> result = prdCuttingService.listTicketItemDetailByCuttingId(cuttingId);
        return Result.success(result);
    }

    @ApiOperation(value = "根据工序条码获取工序详情")
    @GetMapping("/getRouteDetailByBarCode/{barCode}")
    public Result<CuttingRouteDetailVo> getRouteDetailByBarCode(@PathVariable String barCode) {
        CuttingRouteDetailVo result = prdCuttingService.getRouteDetailByBarCode(barCode);
        return Result.success(result);
    }

    @ApiOperation(value = "本月上数数据")
    @GetMapping("/pageCurMonthReport")
    public Result<IPage<CurMonthReportVo>> pageCurMonthReport(Page<CurMonthReportVo> page, CuttingFilterDto cuttingFilterDto) {
        IPage<CurMonthReportVo> result = prdCuttingService.pageCurMonthReport(page, cuttingFilterDto);
        return Result.success(result);
    }

    @ApiOperation(value = "生产进度表")
    @GetMapping("/pageProductionSchedule")
    public Result<Map<String, Object>> pageProductionSchedule(Page<CurMonthReportVo> page, CuttingFilterDto cuttingFilterDto) {
        Map<String, Object> map = prdCuttingService.pageProductionSchedule(page, cuttingFilterDto);
        return Result.success(map);
    }

    @ApiOperation(value = "app产量查询")
    @GetMapping("/appPageProductionSchedule")
    public Result appPageProductionSchedule(Page<CurMonthReportVo> page, CuttingFilterDto cuttingFilterDto) {
        IPage<CurMonthReportVo> curMonthReportVoIPage = prdCuttingService.appPageProductionSchedule(page, cuttingFilterDto);
        return Result.success(curMonthReportVoIPage);
    }

    @ApiOperation(value = "app产量合计查询")
    @PostMapping("/appProductionScheduleSum")
    public Result<JSONObject> appProductionScheduleSum(@RequestBody CuttingFilterDto cuttingFilterDto) {
        JSONObject result = prdCuttingService.appProductionScheduleSum(cuttingFilterDto);
        return Result.success(result);
    }


    @ApiOperation(value = "生产进度表导出")
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/exportProductionSchedule")
    public void exportProductionSchedule(HttpServletResponse response, @RequestBody CuttingFilterDto cuttingFilterDto) throws Exception {
        prdCuttingService.exportProductionSchedule(response, cuttingFilterDto);
    }


    @ApiOperation(value = "本单上数数据")
    @GetMapping("/listReportByCuttingId")
    public Result<List<CurMonthReportVo>> listReportByCuttingId(CuttingFilterDto cuttingFilterDto) {
        List<CurMonthReportVo> result = prdCuttingService.listReportByCuttingId(cuttingFilterDto);
        return Result.success(result);
    }

    @ApiOperation(value = "提交")
    @PutMapping("/submit/{id}")
    public Result<PrdCutting> submit(@PathVariable String id) {
        PrdCutting result = prdCuttingService.submit(id);
        return Result.success(result);
    }

    @PutMapping("/batchSubmitByIds")
    public Result<String> batchSubmitByIds(@RequestBody String[] ids) {
        String result = prdCuttingService.batchSubmitByIds(ids);
        return Result.success(result);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "prdCutting", paramType = "body", dataType = "PrdCutting"),
    })
    @ApiOperation(value = "执行操作 提交/审核/反审核")
    @PostMapping("/doAction")
    public Result<PrdCutting> doAction(@RequestBody PrdCutting prdCutting) throws Exception {
        return prdCuttingService.doAction(prdCutting);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "prdCutting", paramType = "body", dataType = "PrdCutting"),
    })
    @ApiOperation(value = "批量执行操作 提交/审核/反审核")
    @PostMapping("/batchDoAction")
    public Result<String> batchDoAction(@RequestBody PrdCutting prdCutting) throws Exception {
        return prdCuttingService.batchDoAction(prdCutting);
    }

    @PutMapping("/unAudit/{id}")
    public Result<PrdCutting> unAudit(@PathVariable String id) throws Exception {
        PrdCutting result = prdCuttingService.unAudit(id);
        return Result.success(result);
    }

    @PutMapping(value = "/batchUnAuditByIds")
    public Result<String> batchUnAuditByIds(@RequestBody String[] ids) throws Exception {
        String result = prdCuttingService.batchUnAuditByIds(ids);
        return Result.success(result);
    }

    @ApiOperation(value = "获取源单信息")
    @GetMapping("/getSrcData/{srcId}")
    public Result<MoAsSrcDto> getSrcData(@PathVariable String srcId) {
        MoAsSrcDto result = prdCuttingService.getSrcData(srcId);
        return Result.success(result);
    }

    @ApiOperation(value = "app生产进度分页")
    @PostMapping("/pageForProgress")
    public Result<IPage<CuttingTicketDetailVo>> pageForProgress(Page<CuttingTicketDetailVo> page, @RequestBody CuttingFilterDto cuttingFilterDto) {
        IPage<CuttingTicketDetailVo> result = prdCuttingService.pageForProgress(page, cuttingFilterDto);
        return Result.success(result);
    }

    @ApiOperation(value = "app生产进度详情页")
    @GetMapping("/detailForProgress/{ticketId}")
    public Result<CuttingTicketDetailVo> detailForProgress(@PathVariable("ticketId") String ticketId) {
        CuttingTicketDetailVo result = prdCuttingService.detailForProgress(ticketId);
        return Result.success(result);
    }
}
