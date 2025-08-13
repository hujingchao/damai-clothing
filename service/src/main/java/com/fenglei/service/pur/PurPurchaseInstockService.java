package com.fenglei.service.pur;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.common.result.Result;
import com.fenglei.model.pur.entity.PurPurchaseInstock;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface PurPurchaseInstockService extends IService<PurPurchaseInstock> {

    IPage<PurPurchaseInstock> myPage(Page page, PurPurchaseInstock purPurchaseInstock);

    List<PurPurchaseInstock> myList(PurPurchaseInstock purPurchaseInstock);

    PurPurchaseInstock add(PurPurchaseInstock purPurchaseInstock) throws Exception;

    PurPurchaseInstock myUpdate(PurPurchaseInstock purPurchaseInstock) throws Exception;

    Boolean deleteById(Long id);

    void deleteBatch(String[] ids);

    PurPurchaseInstock detail(String id);

    PurPurchaseInstock submit(String id) throws Exception;

    String batchSubmitByIds(String[] ids) throws Exception;

    Result doAction(PurPurchaseInstock purPurchaseInstock) throws Exception;

    Result batchDoAction(PurPurchaseInstock purPurchaseInstock) throws Exception;

    PurPurchaseInstock unAudit(String id) throws Exception;

    String batchUnAuditByIds(String[] ids) throws Exception;

    void itemExport(HttpServletResponse response, PurPurchaseInstock purchaseInstock);
}
