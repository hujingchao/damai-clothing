package com.fenglei.service.prd.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.mapper.prd.PrdMoProcessMapper;
import com.fenglei.model.prd.entity.PrdMoProcess;
import com.fenglei.service.prd.PrdMoProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrdMoProcessServiceImpl extends ServiceImpl<PrdMoProcessMapper, PrdMoProcess> implements PrdMoProcessService {

}
