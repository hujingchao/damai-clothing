package com.fenglei.mapper.pur;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.model.pur.entity.PurPurchaseOrder;
import com.fenglei.model.pur.vo.PurPurchaseOrderItemVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PurPurchaseOrderMapper extends BaseMapper<PurPurchaseOrder> {

    IPage<PurPurchaseOrder> getPage(Page page, @Param("purPurchaseOrder") PurPurchaseOrder purPurchaseOrder);

    List<PurPurchaseOrder> getList(@Param("purPurchaseOrder") PurPurchaseOrder purPurchaseOrder);

    PurPurchaseOrder infoById(String id);
}
