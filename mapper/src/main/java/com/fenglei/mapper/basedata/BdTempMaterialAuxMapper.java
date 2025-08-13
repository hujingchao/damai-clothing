package com.fenglei.mapper.basedata;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenglei.model.basedata.BdTempMaterialAux;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BdTempMaterialAuxMapper extends BaseMapper<BdTempMaterialAux> {

    List<BdTempMaterialAux> getList(@Param("bdTempMaterialAux") BdTempMaterialAux bdTempMaterialAux);
}
