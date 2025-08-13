package com.fenglei.mapper.report;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.model.pur.entity.PurPurchaseInstockItem;
import com.fenglei.model.report.dto.CuttingTicketReportDto;
import com.fenglei.model.report.dto.MaterialsReportDto;
import com.fenglei.model.report.vo.CuttingTicketReportVo;
import com.fenglei.model.report.vo.MaterialsReportVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReportMapper {
    IPage<MaterialsReportVo> materialsReport(Page<MaterialsReportVo> page, @Param("reportDto") MaterialsReportDto reportDto);

    List<MaterialsReportVo> materialsReport( @Param("reportDto") MaterialsReportDto reportDto);

    List<PurPurchaseInstockItem> getPurchaseInstockItemList(@Param("reportDto") MaterialsReportDto reportDto);

    IPage<CuttingTicketReportVo> cuttingTicketReport(Page<CuttingTicketReportVo> page,@Param("reportDto") CuttingTicketReportDto reportDto);

    List<CuttingTicketReportVo> cuttingTicketReport(@Param("reportDto") CuttingTicketReportDto reportDto);
}
