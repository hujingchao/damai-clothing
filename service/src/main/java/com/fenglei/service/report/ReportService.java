package com.fenglei.service.report;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.model.report.dto.CuttingTicketReportDto;
import com.fenglei.model.report.dto.MaterialsReportDto;
import com.fenglei.model.report.vo.CuttingTicketReportVo;
import com.fenglei.model.report.vo.MaterialsReportVo;

import javax.servlet.http.HttpServletResponse;

public interface ReportService {
    IPage<MaterialsReportVo> materialsReportPage(Page<MaterialsReportVo> page, MaterialsReportDto reportDto);

    void materialsReportExport(HttpServletResponse response, MaterialsReportDto reportDto);

    IPage<CuttingTicketReportVo> cuttingTicketReportPage(Page<CuttingTicketReportVo> page, CuttingTicketReportDto reportDto);

    void cuttingTicketReportExport(HttpServletResponse response, CuttingTicketReportDto reportDto);
}
