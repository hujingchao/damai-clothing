package com.fenglei.service.basedata.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.mapper.basedata.BdTempMaterialSpecificationMapper;
import com.fenglei.model.basedata.BdTempMaterialSpecification;
import com.fenglei.service.basedata.BdTempMaterialSpecificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author ljw
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BdTempMaterialSpecificationServiceImpl extends ServiceImpl<BdTempMaterialSpecificationMapper, BdTempMaterialSpecification> implements BdTempMaterialSpecificationService {
}
