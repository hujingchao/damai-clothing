package com.fenglei.service.prd;

import com.fenglei.model.prd.entity.PrdCuttingTicketItemReport;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.model.prd.vo.CuttingRouteDetailVo;

import java.util.List;

/**
 * <p>
 * 裁床单 - 工票信息明细汇报 服务类
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-02
 */
public interface IPrdCuttingTicketItemReportService extends IService<PrdCuttingTicketItemReport> {
    /**
     * 新增
     */
    PrdCuttingTicketItemReport add(PrdCuttingTicketItemReport prdCuttingTicketItemReport);

    /**
     * 批量保存
     */
    List<PrdCuttingTicketItemReport> batchAdd(List<PrdCuttingTicketItemReport> cuttingTicketItemReports);

    /**
     * 删除
     */
    Boolean myRemoveById(String id);

    /**
     * 根据pid删除
     * @param pid 裁床单工票信息明细id
     * @return 是否删除成功
     */
    boolean removeByPid(String pid);

    /**
     * 根据pids删除
     * @param pids 裁床单工票信息明细ids
     * @return 是否删除成功
     */
    boolean removeByPids(List<String> pids);

    /**
     * 批量删除
     */
    Boolean myRemoveByIds(List<String> ids);

    /**
     * 更新
     */
    boolean myUpdate(PrdCuttingTicketItemReport prdCuttingTicketItemReport);

    /**
     * 批量更新
     */
    boolean batchUpdate(List<PrdCuttingTicketItemReport> cuttingTicketItemReports);

    /**
     * 详情
     */
    PrdCuttingTicketItemReport detail(String id);

    /**
     * 根据pid获取详细信息
     */
    List<PrdCuttingTicketItemReport> listDetailByPid(String id);

    /**
     * 根据pids获取详细信息
     */
    List<PrdCuttingTicketItemReport> listDetailByPids(List<String> ids);

    /**
     * 根据cuttingRouteId获取详细信息
     * @param cuttingRouteId 裁床单工序id
     * @return 返回的详细详细列表
     */
    List<PrdCuttingTicketItemReport> listDetailByCuttingRouteId(String cuttingRouteId);

    /**
     * 根据裁床单id查找汇报记录
     * @param cuttingIds 裁床单id
     * @return 汇报记录
     */
    List<PrdCuttingTicketItemReport> listByCuttingIds(List<String> cuttingIds);

    /**
     * 根据裁床单id查找汇报记录
     * @param cuttingId 裁床单id
     * @return 汇报记录
     */
    List<PrdCuttingTicketItemReport> listByCuttingId(String cuttingId);
}
