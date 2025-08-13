package com.fenglei.service.inv;

import com.fenglei.model.inv.entity.InvIoBill;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 物料收发流水账 服务类
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-22
 */
public interface IInvIoBillService extends IService<InvIoBill> {
    /**
     * 新增
     */
    InvIoBill add(InvIoBill invIoBill);

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
    boolean myUpdate(InvIoBill invIoBill);

    /**
     * 分页查询
     */
    IPage<InvIoBill> myPage(Page<InvIoBill> page, InvIoBill invIoBill);

    /**
     * 详情
     */
    InvIoBill detail(String id);

    /**
     * 导出
     */
    void exportInvIoBill(HttpServletResponse response, InvIoBill invIoBill);
}
