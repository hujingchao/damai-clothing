package com.fenglei.service.basedata.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.mapper.basedata.BdMaterialRouteMapper;
import com.fenglei.model.basedata.BdMaterialRoute;
import com.fenglei.model.basedata.dto.StaffSpecialRouteDto;
import com.fenglei.service.basedata.BdMaterialRouteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author ljw
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BdMaterialRouteServiceImpl extends ServiceImpl<BdMaterialRouteMapper, BdMaterialRoute> implements BdMaterialRouteService {

    /**
     * 查找员工特殊工序价格
     * @param productId 商品id
     * @param procedureId 工序id
     * @param staffId 员工id
     */
    @Override
    public StaffSpecialRouteDto getStaffSpecialRouteInfo(String productId, String procedureId, String staffId) {
        return this.baseMapper.getStaffSpecialRouteInfo(productId, procedureId, staffId);
    }
}
