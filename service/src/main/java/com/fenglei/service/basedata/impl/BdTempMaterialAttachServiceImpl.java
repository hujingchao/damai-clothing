package com.fenglei.service.basedata.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.mapper.basedata.BdTempMaterialAttachMapper;
import com.fenglei.model.basedata.BdTempMaterialAttach;
import com.fenglei.service.basedata.BdTempMaterialAttachService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author ljw
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BdTempMaterialAttachServiceImpl extends ServiceImpl<BdTempMaterialAttachMapper, BdTempMaterialAttach> implements BdTempMaterialAttachService {
}
