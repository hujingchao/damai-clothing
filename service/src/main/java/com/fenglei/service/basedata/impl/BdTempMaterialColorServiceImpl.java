package com.fenglei.service.basedata.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.mapper.basedata.BdTempMaterialColorMapper;
import com.fenglei.model.basedata.BdTempMaterialColor;
import com.fenglei.service.basedata.BdTempMaterialColorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author ljw
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BdTempMaterialColorServiceImpl extends ServiceImpl<BdTempMaterialColorMapper, BdTempMaterialColor> implements BdTempMaterialColorService {
}
