package com.fenglei.service.basedata.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.mapper.basedata.BdMaterialAttachMapper;
import com.fenglei.model.basedata.BdMaterialAttach;
import com.fenglei.service.basedata.BdMaterialAttachService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author ljw
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BdMaterialAttachServiceImpl extends ServiceImpl<BdMaterialAttachMapper, BdMaterialAttach> implements BdMaterialAttachService {
}
