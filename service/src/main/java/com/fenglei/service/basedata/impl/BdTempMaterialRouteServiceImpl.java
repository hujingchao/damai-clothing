package com.fenglei.service.basedata.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.mapper.basedata.BdTempMaterialRouteMapper;
import com.fenglei.model.basedata.BdTempMaterialRoute;
import com.fenglei.service.basedata.BdTempMaterialRouteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author ljw
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BdTempMaterialRouteServiceImpl extends ServiceImpl<BdTempMaterialRouteMapper, BdTempMaterialRoute> implements BdTempMaterialRouteService {
}
