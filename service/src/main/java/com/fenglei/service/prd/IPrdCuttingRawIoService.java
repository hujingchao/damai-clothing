package com.fenglei.service.prd;

import com.fenglei.model.prd.entity.PrdCuttingRaw;
import com.fenglei.model.prd.entity.PrdCuttingRawIo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;

/**
 * <p>
 * 裁床单原材料出库记录 服务类
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-15
 */
public interface IPrdCuttingRawIoService extends IService<PrdCuttingRawIo> {
    /**
     * 新增
     */
    PrdCuttingRawIo add(PrdCuttingRawIo prdCuttingRawIo);

    /**
     * 批量新增
     */
    List<PrdCuttingRawIo> batchAdd(List<PrdCuttingRawIo> cuttingRawIos);

    /**
     * 删除
     */
    Boolean myRemoveById(String id);

    /**
     * 批量删除
     */
    Boolean myRemoveByIds(List<String> ids);

    /**
     * 批量删除
     */
    Boolean batchRemove(List<PrdCuttingRawIo> cuttingRawIos);

    /**
     * 根据pids查找
     * @param pids 裁床单原材料id集合
     * @return 结果
     */
    List<PrdCuttingRawIo> listByPids(List<String> pids);
}
