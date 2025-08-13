package com.fenglei.mapper.prd;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.model.prd.dto.CuttingFilterDto;
import com.fenglei.model.prd.entity.PrdCutting;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenglei.model.prd.entity.PrdCuttingTicket;
import com.fenglei.model.prd.vo.CurMonthReportVo;
import com.fenglei.model.prd.vo.CuttingTicketDetailVo;
import com.fenglei.model.prd.vo.ProcedureReportVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 裁床单 - 主体 Mapper 接口
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-02
 */
public interface PrdCuttingMapper extends BaseMapper<PrdCutting> {

    IPage<PrdCutting> myPage(Page page, @Param("cuttingFilterDto") CuttingFilterDto cuttingFilterDto);

    List<PrdCutting> myPage( @Param("cuttingFilterDto") CuttingFilterDto cuttingFilterDto);

    CuttingFilterDto listSum(@Param("cuttingFilterDto") CuttingFilterDto cuttingFilterDto);

    IPage<ProcedureReportVo> pageForReportCount(Page page, @Param("cuttingFilterDto") CuttingFilterDto cuttingFilterDto);

    IPage<CurMonthReportVo> pageCurMonthReport(Page page,  @Param("cuttingFilterDto") CuttingFilterDto cuttingFilterDto);

    List<CurMonthReportVo> listReportByCuttingId(@Param("cuttingFilterDto") CuttingFilterDto cuttingFilterDto);

    List<CurMonthReportVo> pageProductionSchedule(@Param("cuttingFilterDto") CuttingFilterDto cuttingFilterDto);

    IPage<CurMonthReportVo> pageProductionSchedule(Page page, @Param("cuttingFilterDto") CuttingFilterDto cuttingFilterDto);

    IPage<CuttingTicketDetailVo> pageForProgress(Page<CuttingTicketDetailVo> page, @Param("cuttingFilterDto") CuttingFilterDto cuttingFilterDto);

    CuttingTicketDetailVo detailForProgress(@Param("ticketId") String ticketId);

    CuttingFilterDto productionScheduleSum(@Param("cuttingFilterDto") CuttingFilterDto cuttingFilterDto);
}
