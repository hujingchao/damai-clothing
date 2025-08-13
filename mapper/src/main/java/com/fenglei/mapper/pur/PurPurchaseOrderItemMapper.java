package com.fenglei.mapper.pur;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenglei.model.pur.entity.PurPurchaseOrder;
import com.fenglei.model.pur.entity.PurPurchaseOrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PurPurchaseOrderItemMapper extends BaseMapper<PurPurchaseOrderItem> {

    List<PurPurchaseOrderItem> listByPid(@Param("pid") String pid);

    List<PurPurchaseOrderItem> getItemsByMain(@Param("purchaseOrder") PurPurchaseOrder purPurchaseOrder);
}
