package com.fenglei.service.basedata;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.model.basedata.BdMaterialRoute;
import com.fenglei.model.basedata.dto.StaffSpecialRouteDto;

/**
 * @author ljw
 */
public interface BdMaterialRouteService extends IService<BdMaterialRoute> {

    /**
     * 查找员工特殊工序价格
     * @param productId 商品id
     * @param procedureId 工序id
     * @param staffId 员工id
     */
    StaffSpecialRouteDto getStaffSpecialRouteInfo(String productId, String procedureId, String staffId);
}
