package com.fenglei.mapper.basedata;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.model.basedata.BdManualLedger;
import org.apache.ibatis.annotations.Param;

public interface BdManualLedgerMapper extends BaseMapper<BdManualLedger> {

    IPage<BdManualLedger> getPage(Page page, @Param("bdManualLedger") BdManualLedger bdManualLedger);

    BdManualLedger infoById(String id);
}
