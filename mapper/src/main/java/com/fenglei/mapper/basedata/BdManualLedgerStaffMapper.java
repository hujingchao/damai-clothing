package com.fenglei.mapper.basedata;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenglei.model.basedata.BdManualLedgerStaff;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BdManualLedgerStaffMapper extends BaseMapper<BdManualLedgerStaff> {

    List<BdManualLedgerStaff> getList(@Param("bdManualLedgerStaff") BdManualLedgerStaff bdManualLedgerStaff);
}
