package com.fenglei.service.basedata.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.mapper.basedata.BdMaterialSpecificationMapper;
import com.fenglei.model.basedata.BdMaterialSpecification;
import com.fenglei.service.basedata.BdMaterialSpecificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author ljw
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BdMaterialSpecificationServiceImpl extends ServiceImpl<BdMaterialSpecificationMapper, BdMaterialSpecification> implements BdMaterialSpecificationService {
}
