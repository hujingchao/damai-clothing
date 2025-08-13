package com.fenglei.service.basedata;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.model.basedata.BdManualLedger;

import java.util.List;

public interface BdManualLedgerService extends IService<BdManualLedger> {

    IPage<BdManualLedger> myPage(Page page, BdManualLedger bdManualLedger);

    List<BdManualLedger> myList(BdManualLedger bdManualLedger);

    BdManualLedger add(BdManualLedger bdManualLedger) throws Exception;

    BdManualLedger myUpdate(BdManualLedger bdManualLedger) throws Exception;

    Boolean deleteById(Long id);

    void deleteBatch(String[] ids);

    BdManualLedger detail(String id);
}
