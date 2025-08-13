package com.fenglei.service.oms;

import com.fenglei.model.oms.entity.OmsSaleOutStockItemDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;

/**
 * <p>
 * 销售出库分录明细 服务类
 * </p>
 *
 * @author zjn
 * @since 2024-04-15
 */
public interface IOmsSaleOutStockItemDetailService extends IService<OmsSaleOutStockItemDetail> {
    /**
     * 新增
     */
    OmsSaleOutStockItemDetail add(OmsSaleOutStockItemDetail omsSaleOutStockItemDetail);

    /**
     * 删除
     */
    Boolean myRemoveById(String id);

    /**
     * 批量删除
     */
    Boolean myRemoveByIds(List<String> ids);

    /**
     * 更新
     */
    boolean myUpdate(OmsSaleOutStockItemDetail omsSaleOutStockItemDetail);

    /**
     * 分页查询
     */
    IPage<OmsSaleOutStockItemDetail> myPage(Page page, OmsSaleOutStockItemDetail omsSaleOutStockItemDetail);

    /**
     * 详情
     */
    OmsSaleOutStockItemDetail detail(String id);
}
