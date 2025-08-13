package com.fenglei.service.prd;

import com.alibaba.fastjson.JSONObject;
import com.fenglei.common.result.Result;
import com.fenglei.model.prd.dto.DoProcedureReportDto;
import com.fenglei.model.prd.dto.CuttingFilterDto;
import com.fenglei.model.prd.dto.MoAsSrcDto;
import com.fenglei.model.prd.dto.UpdateTicketItemDto;
import com.fenglei.model.prd.entity.PrdCutting;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.model.prd.entity.PrdCuttingTicket;
import com.fenglei.model.prd.entity.PrdCuttingTicketItem;
import com.fenglei.model.prd.entity.PrdCuttingTicketItemReport;
import com.fenglei.model.prd.vo.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 裁床单 - 主体 服务类
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-02
 */
public interface IPrdCuttingService extends IService<PrdCutting> {
    /**
     * 新增
     */
    PrdCutting add(PrdCutting prdCutting);

    /**
     * 删除
     */
    Boolean myRemoveById(String id) throws Exception;

    /**
     * 批量删除
     */
    Boolean myRemoveByIds(List<String> ids) throws Exception;

    /**
     * 更新
     */
    boolean myUpdate(PrdCutting prdCutting) throws Exception;

    /**
     * 分页查询
     */
    IPage<PrdCutting> myPage(Page<PrdCutting> page, CuttingFilterDto cuttingFilterDto);

    void exportPrdCutting(HttpServletResponse response, CuttingFilterDto cuttingFilterDto);

    JSONObject listSum(CuttingFilterDto cuttingFilterDto);

    /**
     * 详情
     */
    PrdCutting detail(String id);

    /**
     * app工序上数列表接口
     * @param id 裁床单id
     */
    List<ProcedureReportVo> detailForProcedureReport(String id);

    /**
     * app工序详情
     * @param routeId 裁床单工序id
     */
    ProcedureReportVo detailCuttingRouteById(String routeId);

    /**
     * app扎号上数列表接口
     * @param id 裁床单id
     */
    List<CuttingTicketItemDetailVo> detailForTicketReport(String id);

    /**
     * 获取编号
     */
    String getNo();

    /**
     * app单工序上数接口
     */
    boolean doProcedureReport(List<DoProcedureReportDto> doProcedureReportDtos);

    /**
     * app多工序上数接口
     */
    boolean doBatchProcedureReport(List<DoProcedureReportDto> doProcedureReportDtos);

    /**
     * app撤数接口
     * @param cuttingTicketItemReportIds 需要撤数的汇报id
     * @return 是否撤数成功
     */
    boolean cancelReport(List<String> cuttingTicketItemReportIds);

    /**
     * app工序改菲接口
     * @param cuttingTicketItemReport 修改后的汇报数据
     * @return 是否修改成功
     */
    boolean updateReport(PrdCuttingTicketItemReport cuttingTicketItemReport);

    /**
     * 根据扎号id获取扎号详情列表
     * @param ticketItemId 扎号id
     * @return 返回的扎号详情列表
     */
    CuttingTicketItemDetailVo getTicketDetailByTicketItemId(String ticketItemId);

    /**
     * 根据扎号获取扎号详情列表
     * @param ticketNo 扎号
     * @return 返回的扎号详情列表
     */
    CuttingTicketItemDetailVo getTicketDetailByTicketNo(String ticketNo);

    /**
     * 根据cuttingId查找详细信息
     * @param cuttingId 裁床单id
     * @return 裁床单票据详情的详细信息的集合
     */
    List<CuttingTicketItemDetailVo> listTicketItemDetailByCuttingId(String cuttingId);

    /**
     * 根据工序条码获取工序详情
     * @param barCode 条码
     * @return 返回的工序详情
     */
    CuttingRouteDetailVo getRouteDetailByBarCode(String barCode);

    /**
     * 本月上数数据
     * @param page 分页数据
     * @param cuttingFilterDto 过滤条件
     * @return 返回的数据
     */
    IPage<CurMonthReportVo> pageCurMonthReport(Page<CurMonthReportVo> page, CuttingFilterDto cuttingFilterDto);

    /**
     * 本单上数数据
     * @param cuttingFilterDto 过滤条件
     * @return 返回的数据
     */
    List<CurMonthReportVo> listReportByCuttingId(CuttingFilterDto cuttingFilterDto);

    PrdCutting submit(String id);

    PrdCutting unAudit(String id) throws Exception;

    String batchUnAuditByIds(String[] ids) throws Exception;

    /**
     * 获取源单信息
     * @param srcId 源单id（生产订单）
     * @return 源单信息Dto
     */
    MoAsSrcDto getSrcData(String srcId);

    /**
     * 反写生产订单-颜色规格详情
     * @param reWriteData 需要反写的信息
     */
    void reWrite(Map<String, BigDecimal> reWriteData);

    /**
     * 批量提交
     * @param ids 需要提交的裁床单id
     */
    String batchSubmitByIds(String[] ids);

    /**
     * 执行操作 提交/审核/反审核
     * @param cutting 裁床单
     * @return 操作执行完成的裁床单
     */
    Result<PrdCutting> doAction(PrdCutting cutting) throws Exception;

    /**
     * 批量执行操作 提交/审核/反审核
     * @param cutting 裁床单
     * @return 操作执行结果
     */
    Result<String> batchDoAction(PrdCutting cutting) throws Exception;

    /**
     * 改菲接口
     * @param updateTicketItemDto 改菲Dto
     * @return 是否改菲成功
     */
    boolean updateTicketItemQty(UpdateTicketItemDto updateTicketItemDto);

    /**
     * 批量改菲接口
     * @param updateTicketItemDtos 改菲Dto
     * @return 是否改菲成功
     */
    boolean batchUpdateTicketItemQty(List<UpdateTicketItemDto> updateTicketItemDtos);

    Map<String, Object> pageProductionSchedule(Page<CurMonthReportVo> page, CuttingFilterDto cuttingFilterDto);

    void exportProductionSchedule(HttpServletResponse response, CuttingFilterDto cuttingFilterDto);

    List<PrdCutting> listDetailByIds( List<String> ids);

    /**
     * 生产进度分页列表
     * @param page 分页参数
     * @param cuttingFilterDto 过滤条件
     */
    IPage<CuttingTicketDetailVo> pageForProgress(Page<CuttingTicketDetailVo> page, CuttingFilterDto cuttingFilterDto);

    /**
     * 生产进度详情
     * @param ticketId 工票信息id
     */
    CuttingTicketDetailVo detailForProgress(String ticketId);

    IPage<CurMonthReportVo> appPageProductionSchedule(Page<CurMonthReportVo> page, CuttingFilterDto cuttingFilterDto);

    JSONObject appProductionScheduleSum(CuttingFilterDto cuttingFilterDto);
}
