package com.fenglei.mapper.basedata;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenglei.model.basedata.BdMaterialAux;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BdMaterialAuxMapper extends BaseMapper<BdMaterialAux> {

    List<BdMaterialAux> getList(@Param("bdMaterialAux") BdMaterialAux bdMaterialAux);
}
