package com.fenglei.service.oms.impl;

import com.fenglei.model.oms.entity.OmsSaleOutStockItemDetail;
import com.fenglei.mapper.oms.OmsSaleOutStockItemDetailMapper;
import com.fenglei.service.oms.IOmsSaleOutStockItemDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * <p>
 * 销售出库分录明细 服务实现类
 * </p>
 *
 * @author zjn
 * @since 2024-04-15
 */
@Service
public class OmsSaleOutStockItemDetailServiceImpl extends ServiceImpl<OmsSaleOutStockItemDetailMapper, OmsSaleOutStockItemDetail> implements IOmsSaleOutStockItemDetailService {

    /**
     * 新增
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OmsSaleOutStockItemDetail add(OmsSaleOutStockItemDetail omsSaleOutStockItemDetail) {
        return null;
    }

    /**
     * 删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean myRemoveById(String id) {
        return null;
    }

    /**
     * 批量删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean myRemoveByIds(List<String> ids) {
        return null;
    }

    /**
     * 更新
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean myUpdate(OmsSaleOutStockItemDetail omsSaleOutStockItemDetail) {
        return true;
    }

    /**
     * 分页查询
     */
    @Override
    public IPage<OmsSaleOutStockItemDetail> myPage(Page page, OmsSaleOutStockItemDetail omsSaleOutStockItemDetail) {
        return null;
    }

    /**
     * 详情
     */
    @Override
    public OmsSaleOutStockItemDetail detail(String id) {
        return null;
    }
}
