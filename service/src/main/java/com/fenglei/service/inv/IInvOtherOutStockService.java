package com.fenglei.service.inv;

import com.fenglei.common.result.Result;
import com.fenglei.model.inv.entity.InvOtherOutStock;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.model.oms.entity.OmsSaleOutStock;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zgm
 * @since 2024-04-28
 */
public interface IInvOtherOutStockService extends IService<InvOtherOutStock> {
    /**
     * 新增
     */
    InvOtherOutStock add(InvOtherOutStock invOtherOutStock);

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
    boolean myUpdate(InvOtherOutStock invOtherOutStock);

    /**
     * 分页查询
     */
    IPage<InvOtherOutStock> myPage(Page page, InvOtherOutStock invOtherOutStock);

    /**
     * 详情
     */
    InvOtherOutStock detail(String id);


    InvOtherOutStock submit(String id);

    String batchSubmitByIds(String[] ids);

    Result doAction(InvOtherOutStock otherOutStock) throws Exception;

    Result batchDoAction(InvOtherOutStock otherOutStock) throws Exception;

    InvOtherOutStock unAudit(String id) throws Exception;

    String batchUnAuditByIds(String[] ids) throws Exception;
}
