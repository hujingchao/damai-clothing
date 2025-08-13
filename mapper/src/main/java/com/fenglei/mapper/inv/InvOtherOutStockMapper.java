package com.fenglei.mapper.inv;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.model.inv.entity.InvOtherOutStock;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zgm
 * @since 2024-04-28
 */
public interface InvOtherOutStockMapper extends BaseMapper<InvOtherOutStock> {

    IPage<InvOtherOutStock> selectOutStock(Page page,@Param("outStock") InvOtherOutStock invOtherOutStock);

    InvOtherOutStock detailOutStock(@Param("id")  String id);
}
