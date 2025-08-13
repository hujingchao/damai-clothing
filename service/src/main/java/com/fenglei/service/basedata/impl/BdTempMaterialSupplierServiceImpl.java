package com.fenglei.service.basedata.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.mapper.basedata.BdTempMaterialSupplierMapper;
import com.fenglei.model.basedata.BdTempMaterialSupplier;
import com.fenglei.service.basedata.BdTempMaterialSupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author ljw
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BdTempMaterialSupplierServiceImpl extends ServiceImpl<BdTempMaterialSupplierMapper, BdTempMaterialSupplier> implements BdTempMaterialSupplierService {
}
