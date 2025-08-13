package com.fenglei.mapper.basedata;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenglei.model.basedata.BdTempMaterialRaw;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BdTempMaterialRawMapper extends BaseMapper<BdTempMaterialRaw> {

    List<BdTempMaterialRaw> getList(@Param("bdTempMaterialRaw") BdTempMaterialRaw bdTempMaterialRaw);
}
