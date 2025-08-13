package com.fenglei.service.prd;

import com.fenglei.model.prd.entity.PrdPacking;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.model.prd.vo.PrdPackingItemVo;

import java.util.List;

/**
 * <p>
 * 包装单 服务类
 * </p>
 *
 * @author zgm
 * @since 2024-04-11
 */
public interface IPrdPackingService extends IService<PrdPacking> {
    /**
     * 新增
     */
    PrdPacking add(PrdPacking prdPacking);

    String getNo();

    /**
     * 删除
     */
    Boolean myRemoveById(String id);

    /**
     * 批量删除
     */
    Boolean myRemoveByIds(List<String> ids);



    /**
     * 分页查询
     */
    IPage<PrdPacking> myPage(Page page, PrdPacking prdPacking);


    /**
     * 子项分页查询
     */
    IPage<PrdPackingItemVo> wxPage(Page page, PrdPackingItemVo itemVo);

    List<Integer> getStatusCountAll(PrdPackingItemVo itemVo);

    /**
     * 详情
     */
    PrdPacking itemDetail(String id, String itemId);

    boolean itemSetTop(String id,Integer setTop);

    boolean itemRemove(String id);

    boolean itemUpdate(PrdPacking prdPacking);

}
