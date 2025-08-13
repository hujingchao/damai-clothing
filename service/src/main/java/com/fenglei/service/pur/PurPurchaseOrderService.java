package com.fenglei.service.pur;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.common.result.Result;
import com.fenglei.model.pur.entity.PurPurchaseOrder;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface PurPurchaseOrderService extends IService<PurPurchaseOrder> {

    IPage<PurPurchaseOrder> myPage(Page<PurPurchaseOrder> page, PurPurchaseOrder purPurchaseOrder);

    List<PurPurchaseOrder> myList(PurPurchaseOrder purPurchaseOrder);

    PurPurchaseOrder add(PurPurchaseOrder purPurchaseOrder) throws Exception;

    PurPurchaseOrder myUpdate(PurPurchaseOrder purPurchaseOrder) throws Exception;

    Boolean deleteById(Long id);

    void deleteBatch(String[] ids);

    PurPurchaseOrder detail(String id);

    PurPurchaseOrder submit(String id) throws Exception;

    String batchSubmitByIds(String[] ids) throws Exception;

    Result doAction(PurPurchaseOrder purPurchaseOrder) throws Exception;

    Result batchDoAction(PurPurchaseOrder purPurchaseOrder) throws Exception;

    PurPurchaseOrder unAudit(String id) throws Exception;

    String batchUnAuditByIds(String[] ids) throws Exception;

    void itemExport(HttpServletResponse response, PurPurchaseOrder purchaseOrder);
}
