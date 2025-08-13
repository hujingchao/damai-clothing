package com.fenglei.mapper.pur;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenglei.model.pur.entity.PurPurchaseInstock;
import com.fenglei.model.pur.entity.PurPurchaseInstockItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PurPurchaseInstockItemMapper extends BaseMapper<PurPurchaseInstockItem> {

    List<PurPurchaseInstockItem> listByPid(@Param("pid") String pid);

    List<PurPurchaseInstockItem> getItemsByMain(@Param("purchaseInstock") PurPurchaseInstock purPurchaseInstock);
}
