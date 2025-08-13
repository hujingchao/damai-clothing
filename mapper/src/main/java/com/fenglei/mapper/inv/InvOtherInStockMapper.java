package com.fenglei.mapper.inv;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.model.inv.entity.InvOtherInStock;
import com.fenglei.model.inv.entity.InvOtherOutStock;
import org.apache.ibatis.annotations.Param;

public interface InvOtherInStockMapper extends BaseMapper<InvOtherInStock> {

    IPage<InvOtherInStock> selectInStock(Page page, @Param("inStock") InvOtherInStock invOtherInStock);

    InvOtherInStock detailInStock(@Param("id")  String id);
}
