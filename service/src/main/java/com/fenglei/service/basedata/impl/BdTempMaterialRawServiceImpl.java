package com.fenglei.service.basedata.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.mapper.basedata.BdTempMaterialRawMapper;
import com.fenglei.model.basedata.BdTempMaterialRaw;
import com.fenglei.service.basedata.BdTempMaterialRawService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author ljw
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BdTempMaterialRawServiceImpl extends ServiceImpl<BdTempMaterialRawMapper, BdTempMaterialRaw> implements BdTempMaterialRawService {
}
