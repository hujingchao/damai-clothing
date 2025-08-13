package com.fenglei.service.prd;

import com.fenglei.model.prd.entity.PrdInStock;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.model.prd.entity.PrdInStockItem;
import com.fenglei.model.prd.vo.PrdInStockItemVo;
import com.fenglei.model.prd.vo.PrdPackingItemVo;

import java.util.List;

/**
 * <p>
 * 包装单 服务类
 * </p>
 *
 * @author zgm
 * @since 2024-04-18
 */
public interface IPrdInStockService extends IService<PrdInStock> {
    /**
     * 新增
     */
    PrdInStock add(PrdInStock prdInStock);

    String getNo();

    /**
     * 删除
     */
    Boolean myRemoveById(String id);





    /**
     * 分页查询
     */
    IPage<PrdInStock> myPage(Page page, PrdInStock prdInStock);

    /**
     * 子项分页查询
     */
    IPage<PrdInStockItemVo> wxPage(Page page, PrdInStockItemVo itemVo);

    List<Integer> getStatusCountAll(PrdInStockItemVo itemVo);




    /**
     * 详情
     */
    PrdInStock itemDetail(String id, String itemId);

    boolean itemSetTop(String id, Integer setTop);

    boolean itemRemove(String id);

    boolean itemUpdate(PrdInStock prdInStock);

    Boolean checkInStockCount(List<PrdInStockItem> itemList);
}
