package com.fenglei.service.basedata.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.mapper.basedata.BdManualLedgerProcedureMapper;
import com.fenglei.model.basedata.BdManualLedgerProcedure;
import com.fenglei.service.basedata.BdManualLedgerProcedureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author ljw
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BdManualLedgerProcedureServiceImpl extends ServiceImpl<BdManualLedgerProcedureMapper, BdManualLedgerProcedure> implements BdManualLedgerProcedureService {
}
