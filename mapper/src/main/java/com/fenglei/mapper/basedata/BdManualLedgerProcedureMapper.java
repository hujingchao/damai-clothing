package com.fenglei.mapper.basedata;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenglei.model.basedata.BdManualLedgerProcedure;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BdManualLedgerProcedureMapper extends BaseMapper<BdManualLedgerProcedure> {

    List<BdManualLedgerProcedure> getList(@Param("bdManualLedgerProcedure") BdManualLedgerProcedure bdManualLedgerProcedure);
}
