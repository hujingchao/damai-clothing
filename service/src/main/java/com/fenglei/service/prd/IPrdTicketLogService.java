package com.fenglei.service.prd;

import com.fenglei.model.prd.entity.PrdTicketLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhaojunnan
 * @since 2024-05-06
 */
public interface IPrdTicketLogService extends IService<PrdTicketLog> {
    /**
     * 新增
     */
    PrdTicketLog add(PrdTicketLog prdTicketLog);

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
    boolean myUpdate(PrdTicketLog prdTicketLog);

    /**
     * 分页查询
     */
    IPage<PrdTicketLog> myPage(Page page, PrdTicketLog prdTicketLog);

    /**
     * 详情
     */
    PrdTicketLog detail(String id);
}
