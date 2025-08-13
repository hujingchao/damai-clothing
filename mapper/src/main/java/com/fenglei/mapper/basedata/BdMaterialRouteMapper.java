package com.fenglei.mapper.basedata;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenglei.model.basedata.BdMaterialRoute;
import com.fenglei.model.basedata.BdMaterialRouteSpecial;
import com.fenglei.model.basedata.dto.StaffSpecialRouteDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BdMaterialRouteMapper extends BaseMapper<BdMaterialRoute> {

    List<BdMaterialRoute> getListWithSpecial(@Param("bdMaterialRouteSpecial") BdMaterialRouteSpecial bdMaterialRouteSpecial);

    List<BdMaterialRoute> getList(@Param("bdMaterialRoute") BdMaterialRoute bdMaterialRoute);

    StaffSpecialRouteDto getStaffSpecialRouteInfo(@Param("productId") String productId, @Param("procedureId") String procedureId, @Param("staffId") String staffId);
}
