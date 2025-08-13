package com.fenglei.service.basedata.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.mapper.basedata.BdMaterialRawMapper;
import com.fenglei.model.basedata.BdMaterialRaw;
import com.fenglei.service.basedata.BdMaterialRawService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author ljw
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BdMaterialRawServiceImpl extends ServiceImpl<BdMaterialRawMapper, BdMaterialRaw> implements BdMaterialRawService {
}
