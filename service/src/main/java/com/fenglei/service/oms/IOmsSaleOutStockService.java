package com.fenglei.service.oms;

import com.fenglei.common.result.Result;
import com.fenglei.model.oms.entity.OmsSaleOutStock;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.model.oms.entity.dto.OmsSaleOutStockDTO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 销售出库单 服务类
 * </p>
 *
 * @author zjn
 * @since 2024-04-15
 */
public interface IOmsSaleOutStockService extends IService<OmsSaleOutStock> {
    /**
     * 新增
     */
    OmsSaleOutStock add(OmsSaleOutStock omsSaleOutStock);

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
    boolean myUpdate(OmsSaleOutStock omsSaleOutStock);

    /**
     * 分页查询
     */
    IPage<OmsSaleOutStock> myPage(Page page, OmsSaleOutStock omsSaleOutStock);

    /**
     * 详情
     */
    OmsSaleOutStock detail(String id);

    OmsSaleOutStock submit(String id);

    String batchSubmitByIds(String[] ids);

    Result doAction(OmsSaleOutStock omsSaleOutStock) throws Exception;

    Result batchDoAction(OmsSaleOutStock omsSaleOutStock) throws Exception;

    OmsSaleOutStock unAudit(String id) throws Exception;

    String batchUnAuditByIds(String[] ids) throws Exception;

    List<Integer> getStatusCountAll(OmsSaleOutStock omsSaleOutStock);

    IPage<OmsSaleOutStockDTO> getOutStockSummary(Page page, OmsSaleOutStockDTO omsSaleOutStockDTO);

    void exportOutStockSummary(HttpServletResponse response, OmsSaleOutStockDTO omsSaleOutStockDTO);
}
