package com.fenglei.mapper.basedata;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenglei.model.basedata.BdTempMaterialRouteSpecial;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BdTempMaterialRouteSpecialMapper extends BaseMapper<BdTempMaterialRouteSpecial> {

    List<BdTempMaterialRouteSpecial> getList(@Param("bdTempMaterialRouteSpecial") BdTempMaterialRouteSpecial bdTempMaterialRouteSpecial);
}
