package com.fenglei.service.basedata.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.mapper.basedata.BdTempMaterialProcessMapper;
import com.fenglei.model.basedata.BdTempMaterialProcess;
import com.fenglei.service.basedata.BdTempMaterialProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author ljw
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BdTempMaterialProcessServiceImpl extends ServiceImpl<BdTempMaterialProcessMapper, BdTempMaterialProcess> implements BdTempMaterialProcessService {
}
