package com.fenglei.service.prd;

import com.fenglei.model.prd.entity.PrdInStockItem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;

/**
 * <p>
 * 包装单分录 服务类
 * </p>
 *
 * @author zgm
 * @since 2024-04-18
 */
public interface IPrdInStockItemService extends IService<PrdInStockItem> {
    /**
     * 新增
     */
    PrdInStockItem add(PrdInStockItem prdInStockItem);

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
    boolean myUpdate(PrdInStockItem prdInStockItem);

    /**
     * 分页查询
     */
    IPage<PrdInStockItem> myPage(Page page, PrdInStockItem prdInStockItem);

    /**
     * 详情
     */
    PrdInStockItem detail(String id);
}
