package com.fenglei.service.basedata.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.mapper.basedata.BdMaterialAuxMapper;
import com.fenglei.model.basedata.BdMaterialAux;
import com.fenglei.service.basedata.BdMaterialAuxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author ljw
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BdMaterialAuxServiceImpl extends ServiceImpl<BdMaterialAuxMapper, BdMaterialAux> implements BdMaterialAuxService {
}
