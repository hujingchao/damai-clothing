package com.fenglei.service.prd;

import com.fenglei.model.prd.entity.PrdCuttingRoute;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 裁床单 - 工序 服务类
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-03
 */
public interface IPrdCuttingRouteService extends IService<PrdCuttingRoute> {
    /**
     * 新增
     */
    PrdCuttingRoute add(PrdCuttingRoute prdCuttingRoute);

    /**
     * 批量新增
     */
    List<PrdCuttingRoute> batchAdd(List<PrdCuttingRoute> cuttingRoutes);

    /**
     * 删除
     */
    Boolean myRemoveById(String id);

    /**
     * 根据pid删除
     */
    boolean removeByPid(String pid);

    /**
     * 根据pids删除
     */
    boolean removeByPids(List<String> pids);

    /**
     * 批量删除
     */
    Boolean myRemoveByIds(List<String> ids);

    /**
     * 更新
     */
    boolean myUpdate(PrdCuttingRoute prdCuttingRoute);

    /**
     * 批量更新
     */
    boolean batchSaveOrUpdate(List<PrdCuttingRoute> cuttingRoutes);

    /**
     * 详情
     */
    PrdCuttingRoute detail(String id);

    /**
     * 根据pid查询
     */
    List<PrdCuttingRoute> listByPid(String pid);

    /**
     * 根据pids查询
     */
    List<PrdCuttingRoute> listByPids(Collection<String> pids);

    List<PrdCuttingRoute> listDetailByIds(List<String> routeIds);
}
