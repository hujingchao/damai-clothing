package com.fenglei.service.oms;

import com.fenglei.model.oms.entity.OmsSaleOutStockItem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;

/**
 * <p>
 * 销售出库单分录 服务类
 * </p>
 *
 * @author zjn
 * @since 2024-04-15
 */
public interface IOmsSaleOutStockItemService extends IService<OmsSaleOutStockItem> {
    /**
     * 新增
     */
    OmsSaleOutStockItem add(OmsSaleOutStockItem omsSaleOutStockItem);

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
    boolean myUpdate(OmsSaleOutStockItem omsSaleOutStockItem);

    /**
     * 分页查询
     */
    IPage<OmsSaleOutStockItem> myPage(Page page, OmsSaleOutStockItem omsSaleOutStockItem);

    /**
     * 详情
     */
    OmsSaleOutStockItem detail(String id);
}
