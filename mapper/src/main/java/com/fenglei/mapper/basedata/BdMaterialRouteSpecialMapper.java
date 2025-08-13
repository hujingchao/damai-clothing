package com.fenglei.mapper.basedata;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenglei.model.basedata.BdMaterialRouteSpecial;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BdMaterialRouteSpecialMapper extends BaseMapper<BdMaterialRouteSpecial> {

    List<BdMaterialRouteSpecial> getList(@Param("bdMaterialRouteSpecial") BdMaterialRouteSpecial bdMaterialRouteSpecial);
}
