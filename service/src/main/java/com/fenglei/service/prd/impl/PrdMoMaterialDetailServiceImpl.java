package com.fenglei.service.prd.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.mapper.prd.PrdMoMaterialDetailMapper;
import com.fenglei.model.prd.entity.PrdMoMaterialDetail;
import com.fenglei.service.prd.PrdMoMaterialDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrdMoMaterialDetailServiceImpl extends ServiceImpl<PrdMoMaterialDetailMapper, PrdMoMaterialDetail> implements PrdMoMaterialDetailService {

}
