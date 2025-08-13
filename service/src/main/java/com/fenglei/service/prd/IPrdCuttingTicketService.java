package com.fenglei.service.prd;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.model.prd.entity.PrdCuttingTicket;
import com.fenglei.model.prd.vo.CuttingRouteDetailVo;

import java.util.List;

/**
 * <p>
 * 裁床单 - 工票信息 服务类
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-02
 */
public interface IPrdCuttingTicketService extends IService<PrdCuttingTicket> {

    /**
     * 批量保存工票信息
     *
     * @param cuttingTickets 需要保存的工票信息
     * @return 已经保存的工票信息
     */
    List<PrdCuttingTicket> batchAdd(List<PrdCuttingTicket> cuttingTickets);

    /**
     * 根据pid删除
     *
     * @param pid 裁床单id
     * @return 是否删除成功
     */
    boolean removeByPid(String pid);

    /**
     * 根据pid删除
     *
     * @param pids 裁床单ids
     * @return 是否删除成功
     */
    boolean removeByPids(List<String> pids);

    /**
     * 批量修改工票信息
     *
     * @param cuttingTickets 需要修改的工票信息
     * @return 是否修改成功
     */
    boolean batchSaveOrUpdate(List<PrdCuttingTicket> cuttingTickets);

    /**
     * 根据pid查询列表
     *
     * @param pid 裁床单id
     * @return 裁床单工票信息
     */
    List<PrdCuttingTicket> listByPid(String pid);

    /**
     * 根据pids查询列表
     *
     * @param pids 裁床单ids
     * @return 裁床单工票信息
     */
    List<PrdCuttingTicket> listByPids(List<String> pids);

    /**
     * 根据pid查询详情
     *
     * @param pid 裁床单id
     * @return 裁床单工票信息详情
     */
    List<PrdCuttingTicket> listDetailByPid(String pid);

    /**
     * 根据裁床单工序id获取裁床单工票信息明细汇报列表
     *
     * @param cuttingRouteId 裁床单工序id
     * @return 返回的裁床单工票信息明细汇报列表
     */
    List<CuttingRouteDetailVo> listRouteDetailByCuttingRouteId(String cuttingRouteId);


    List<PrdCuttingTicket> listDetailByIds(List<String> ids);

}
