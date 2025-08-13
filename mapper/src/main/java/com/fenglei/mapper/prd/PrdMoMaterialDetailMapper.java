package com.fenglei.mapper.prd;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenglei.model.prd.entity.PrdMoMaterialDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PrdMoMaterialDetailMapper extends BaseMapper<PrdMoMaterialDetail> {

    List<PrdMoMaterialDetail> getList(@Param("moMaterialDetail") PrdMoMaterialDetail prdMoMaterialDetail);

    List<PrdMoMaterialDetail> getListOtherBill(@Param("moMaterialDetail") PrdMoMaterialDetail prdMoMaterialDetail);
}
