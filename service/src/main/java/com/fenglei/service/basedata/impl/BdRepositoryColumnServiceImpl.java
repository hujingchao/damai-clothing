package com.fenglei.service.basedata.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.mapper.basedata.BdRepositoryColumnMapper;
import com.fenglei.model.basedata.BdRepositoryColumn;
import com.fenglei.service.basedata.BdRepositoryColumnService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author ljw
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BdRepositoryColumnServiceImpl extends ServiceImpl<BdRepositoryColumnMapper, BdRepositoryColumn> implements BdRepositoryColumnService {
}
