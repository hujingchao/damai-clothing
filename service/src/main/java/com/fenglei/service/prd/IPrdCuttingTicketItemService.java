package com.fenglei.service.prd;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.model.prd.entity.PrdCuttingTicketItem;

import java.util.List;

/**
 * <p>
 * 裁床单 - 工票信息明细 服务类
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-02
 */
public interface IPrdCuttingTicketItemService extends IService<PrdCuttingTicketItem> {

    /**
     * 详情
     */
    PrdCuttingTicketItem detail(String id);

    /**
     * 详情
     */
    PrdCuttingTicketItem detailByTicketNo(String ticketNo);

    /**
     * 根据pid查找
     * @param pid 裁床单票据信息id
     * @return 裁床单票据详情集合
     */
    List<PrdCuttingTicketItem> listbyPid(String pid);

    /**
     * 根据pids查找
     * @param pids 裁床单票据信息ids
     * @return 裁床单票据详情集合
     */
    List<PrdCuttingTicketItem> listbyPids(List<String> pids);

    /**
     * 根据gpid查找
     * @param gpId 裁床单id
     * @return 裁床单票据详情集合
     */
    List<PrdCuttingTicketItem> listByGpId(String gpId);

    /**
     * 根据gpids查找
     * @param gpIds 裁床单ids
     * @return 裁床单票据详情集合
     */
    List<PrdCuttingTicketItem> listByGpIds(List<String> gpIds);

    /**
     * 根据pid查找
     * @param pid 裁床单票据id
     * @return 裁床单票据详情集合
     */
    List<PrdCuttingTicketItem> listDetailById(String pid);

    /**
     * 根据pid查找
     * @param pids 裁床单票据ids
     * @return 裁床单票据详情集合
     */
    List<PrdCuttingTicketItem> listDetailByPids(List<String> pids);


    List<PrdCuttingTicketItem> listDetailByIds(List<String> ids);

    /**
     * 批量保存工票信息明细
     *
     * @param cuttingTicketItems 需要保存的工票信息明细
     * @return 已经保存的工票信息明细
     */
    List<PrdCuttingTicketItem> batchAdd(List<PrdCuttingTicketItem> cuttingTicketItems);

    /**
     * 根据pid删除票据详情
     *
     * @param pid 票据id
     * @return 是否删除成功
     */
    boolean removeByPid(String pid);

    /**
     * 根据pids删除票据详情
     *
     * @param pids 票据ids
     * @return 是否删除成功
     */
    boolean removeByPids(List<String> pids);

    /**
     * 根据gpId删除数据详情
     *
     * @param gpId 裁床单id
     * @return 是否删除成功
     */
    boolean removeByGpId(String gpId);

    /**
     * 根据gpId删除数据
     *
     * @param gpIds 裁床单id
     * @return 是否删除成功
     */
    boolean removeByGpIds(List<String> gpIds);

    /**
     * 批量更新票据详情
     *
     * @param cuttingTicketItems 需要更新的票据详情
     * @return 是否更新成功
     */
    boolean batchSaveOrUpdate(List<PrdCuttingTicketItem> cuttingTicketItems);
}
