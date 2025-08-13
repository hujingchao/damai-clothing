package com.fenglei.service.basedata.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.mapper.basedata.BdMaterialColorMapper;
import com.fenglei.model.basedata.BdMaterialColor;
import com.fenglei.service.basedata.BdMaterialColorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author ljw
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BdMaterialColorServiceImpl extends ServiceImpl<BdMaterialColorMapper, BdMaterialColor> implements BdMaterialColorService {
}
