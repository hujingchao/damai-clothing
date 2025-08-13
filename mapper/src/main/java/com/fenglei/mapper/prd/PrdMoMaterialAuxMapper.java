package com.fenglei.mapper.prd;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenglei.model.prd.entity.PrdMoMaterialAux;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PrdMoMaterialAuxMapper extends BaseMapper<PrdMoMaterialAux> {

    List<PrdMoMaterialAux> getList(@Param("moMaterialAux") PrdMoMaterialAux prdMoMaterialAux);

    List<PrdMoMaterialAux> getListOtherBill(@Param("moMaterialAux") PrdMoMaterialAux prdMoMaterialAux);

}
