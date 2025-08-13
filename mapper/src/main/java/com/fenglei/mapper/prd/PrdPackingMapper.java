package com.fenglei.mapper.prd;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.model.prd.entity.PrdPacking;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenglei.model.prd.vo.PrdPackingItemVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 包装单 Mapper 接口
 * </p>
 *
 * @author zgm
 * @since 2024-04-11
 */
public interface PrdPackingMapper extends BaseMapper<PrdPacking> {

    IPage<PrdPackingItemVo> wxPage(Page page, @Param("itemVo") PrdPackingItemVo itemVo);

    List<PrdPackingItemVo> wxPage(@Param("itemVo") PrdPackingItemVo itemVo);

    PrdPacking itemDetail(@Param("id")  String id, @Param("itemId")  String itemId);
}
