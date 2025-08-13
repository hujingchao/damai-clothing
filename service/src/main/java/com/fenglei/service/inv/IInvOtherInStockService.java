package com.fenglei.service.inv;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.common.result.Result;
import com.fenglei.model.inv.entity.InvOtherInStock;

import java.util.List;

public interface IInvOtherInStockService extends IService<InvOtherInStock> {
    /**
     * 新增
     */
    InvOtherInStock add(InvOtherInStock invOtherInStock);

    /**
     * 删除
     */
    Boolean myRemoveById(String id);

    /**
     * 批量删除
     */
    Boolean myRemoveByIds(List<String> ids);

    /**
     * 更新
     */
    boolean myUpdate(InvOtherInStock invOtherInStock);

    /**
     * 分页查询
     */
    IPage<InvOtherInStock> myPage(Page page, InvOtherInStock invOtherInStock);

    /**
     * 详情
     */
    InvOtherInStock detail(String id);


    InvOtherInStock submit(String id);

    String batchSubmitByIds(String[] ids);

    Result doAction(InvOtherInStock otherInStock) throws Exception;

    Result batchDoAction(InvOtherInStock otherInStock) throws Exception;

    InvOtherInStock unAudit(String id) throws Exception;

    String batchUnAuditByIds(String[] ids) throws Exception;
}
