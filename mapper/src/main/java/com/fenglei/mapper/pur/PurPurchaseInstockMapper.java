package com.fenglei.mapper.pur;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.model.pur.entity.PurPurchaseInstock;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PurPurchaseInstockMapper extends BaseMapper<PurPurchaseInstock> {

    IPage<PurPurchaseInstock> getPage(Page page, @Param("purPurchaseInstock") PurPurchaseInstock purPurchaseInstock);

    List<PurPurchaseInstock> getList(@Param("purPurchaseInstock") PurPurchaseInstock purPurchaseInstock);

    PurPurchaseInstock infoById(String id);
}
