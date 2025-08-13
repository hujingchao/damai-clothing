package com.fenglei.service.prd;

import com.fenglei.model.prd.entity.PrdCuttingRaw;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;

/**
 * <p>
 * 裁床单 - 原材料信息 服务类
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-11
 */
public interface IPrdCuttingRawService extends IService<PrdCuttingRaw> {

    /**
     * 批量新增
     */
    List<PrdCuttingRaw> batchAdd(List<PrdCuttingRaw> cuttingRaws);

    /**
     * 根据pid删除
     * @param pid 裁床单id
     */
    Boolean removeByPid(String pid);

    /**
     * 根据pids删除
     * @param pids 裁床单ids
     */
    Boolean removeByPids(List<String> pids);

    /**
     * 批量保存更新
     */
    Boolean batchSaveOrUpdate(List<PrdCuttingRaw> cuttingRaws);

    /**
     * 根据pids查询
     */
    List<PrdCuttingRaw> listByPid(String pid);
}
