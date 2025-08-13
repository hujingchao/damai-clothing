package com.fenglei.service.basedata.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.mapper.basedata.BdTempMaterialDetailMapper;
import com.fenglei.model.basedata.BdTempMaterialDetail;
import com.fenglei.service.basedata.BdTempMaterialDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author ljw
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BdTempMaterialDetailServiceImpl extends ServiceImpl<BdTempMaterialDetailMapper, BdTempMaterialDetail> implements BdTempMaterialDetailService {
}
