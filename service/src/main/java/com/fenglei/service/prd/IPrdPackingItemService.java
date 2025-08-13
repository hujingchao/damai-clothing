package com.fenglei.service.prd;

import com.fenglei.model.prd.entity.PrdPackingItem;
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
 * @since 2024-04-11
 */
public interface IPrdPackingItemService extends IService<PrdPackingItem> {
    /**
     * 新增
     */
    PrdPackingItem add(PrdPackingItem prdPackingItem);

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
    boolean myUpdate(PrdPackingItem prdPackingItem);

    /**
     * 分页查询
     */
    IPage<PrdPackingItem> myPage(Page page, PrdPackingItem prdPackingItem);

    /**
     * 详情
     */
    PrdPackingItem detail(String id);
}
