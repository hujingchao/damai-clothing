package com.fenglei.service.basedata.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.mapper.basedata.BdRepositoryShelfMapper;
import com.fenglei.model.basedata.BdRepositoryShelf;
import com.fenglei.service.basedata.BdRepositoryShelfService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author ljw
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BdRepositoryShelfServiceImpl extends ServiceImpl<BdRepositoryShelfMapper, BdRepositoryShelf> implements BdRepositoryShelfService {
}
