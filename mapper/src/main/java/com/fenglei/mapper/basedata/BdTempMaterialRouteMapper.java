package com.fenglei.mapper.basedata;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenglei.model.basedata.BdTempMaterialRoute;
import com.fenglei.model.basedata.BdTempMaterialRouteSpecial;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BdTempMaterialRouteMapper extends BaseMapper<BdTempMaterialRoute> {

    List<BdTempMaterialRoute> getListWithSpecial(@Param("bdTempMaterialRouteSpecial") BdTempMaterialRouteSpecial bdTempMaterialRouteSpecial);

    List<BdTempMaterialRoute> getList(@Param("bdTempMaterialRoute") BdTempMaterialRoute bdTempMaterialRoute);
}
