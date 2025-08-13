package com.fenglei.mapper.prd;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.model.prd.entity.PrdInStock;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenglei.model.prd.entity.PrdPacking;
import com.fenglei.model.prd.vo.PrdInStockItemVo;
import com.fenglei.model.prd.vo.PrdPackingItemVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 包装单 Mapper 接口
 * </p>
 *
 * @author zgm
 * @since 2024-04-18
 */
public interface PrdInStockMapper extends BaseMapper<PrdInStock> {

    IPage<PrdInStockItemVo> wxPage(Page page,@Param("itemVo") PrdInStockItemVo itemVo);
    List<PrdInStockItemVo> wxPage(@Param("itemVo") PrdInStockItemVo itemVo);
    PrdInStock itemDetail(@Param("id")  String id, @Param("itemId")  String itemId);
}
