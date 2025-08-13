package com.fenglei.service.prd;

import com.fenglei.model.prd.entity.PrdCuttingAuxIo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;

/**
 * <p>
 * 裁床单辅料出库记录 服务类
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-17
 */
public interface IPrdCuttingAuxIoService extends IService<PrdCuttingAuxIo> {

    /**
     * 批量新增
     */
    List<PrdCuttingAuxIo> batchAdd(List<PrdCuttingAuxIo> cuttingAuxIos);

    /**
     * 根据pid查找
     * @param pid 裁床单id
     */
    List<PrdCuttingAuxIo> listByPid(String pid);

    /**
     * 批量删除
     */
    boolean batchRemove(List<PrdCuttingAuxIo> cuttingAuxIos);
}
