package com.fenglei.service.basedata.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.mapper.basedata.BdManualLedgerStaffMapper;
import com.fenglei.model.basedata.BdManualLedgerStaff;
import com.fenglei.service.basedata.BdManualLedgerStaffService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author ljw
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BdManualLedgerStaffServiceImpl extends ServiceImpl<BdManualLedgerStaffMapper, BdManualLedgerStaff> implements BdManualLedgerStaffService {
}
