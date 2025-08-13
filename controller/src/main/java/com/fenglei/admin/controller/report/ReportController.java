package com.fenglei.admin.controller.report;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.result.Result;
import com.fenglei.model.report.dto.CuttingTicketReportDto;
import com.fenglei.model.report.dto.MaterialsReportDto;
import com.fenglei.model.report.vo.CuttingTicketReportVo;
import com.fenglei.model.report.vo.MaterialsReportVo;
import com.fenglei.service.report.ReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@Api(tags = "报表接口")
@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    @Resource
    private ReportService reportService;


    @ApiOperation(value = "来料汇总表分页", notes = "来料汇总表分页")
    @GetMapping("/materialsReportPage")
    public Result materialsReportPage(Page<MaterialsReportVo> page, MaterialsReportDto reportDto) throws Exception {
        IPage<MaterialsReportVo> iPage = reportService.materialsReportPage(page, reportDto);
        return Result.success(iPage);
    }

    @ApiOperation(value = "来料汇总表导出", notes = "来料汇总表导出")
    @RequestMapping(value = "/materialsReportExport", method = RequestMethod.GET)
    @ResponseBody
    public void materialsReportExport(Page<MaterialsReportVo> page, HttpServletResponse response,MaterialsReportDto reportDto) throws Exception {
        reportService.materialsReportExport(response, reportDto);
    }

    @ApiOperation(value = "裁床统计报表分页", notes = "裁床统计报表分页")
    @GetMapping("/cuttingTicketReportPage")
    public Result cuttingTicketReportPage(Page<CuttingTicketReportVo> page, CuttingTicketReportDto reportDto) throws Exception {
        IPage<CuttingTicketReportVo> iPage = reportService.cuttingTicketReportPage(page, reportDto);
        return Result.success(iPage);
    }


    @ApiOperation(value = "裁床统计报表导出", notes = "裁床统计报表导出")
    @GetMapping("/cuttingTicketReportExport")
    public void cuttingTicketReportExport(HttpServletResponse response, CuttingTicketReportDto reportDto) throws Exception {
         reportService.cuttingTicketReportExport(response, reportDto);
    }



}
