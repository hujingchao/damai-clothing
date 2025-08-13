package com.fenglei.service.oms.impl;

import com.fenglei.model.oms.entity.OmsSaleOutStockItem;
import com.fenglei.mapper.oms.OmsSaleOutStockItemMapper;
import com.fenglei.service.oms.IOmsSaleOutStockItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * <p>
 * 销售出库单分录 服务实现类
 * </p>
 *
 * @author zjn
 * @since 2024-04-15
 */
@Service
public class OmsSaleOutStockItemServiceImpl extends ServiceImpl<OmsSaleOutStockItemMapper, OmsSaleOutStockItem> implements IOmsSaleOutStockItemService {

    /**
     * 新增
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OmsSaleOutStockItem add(OmsSaleOutStockItem omsSaleOutStockItem) {
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
    public boolean myUpdate(OmsSaleOutStockItem omsSaleOutStockItem) {
        return true;
    }

    /**
     * 分页查询
     */
    @Override
    public IPage<OmsSaleOutStockItem> myPage(Page page, OmsSaleOutStockItem omsSaleOutStockItem) {
        return null;
    }

    /**
     * 详情
     */
    @Override
    public OmsSaleOutStockItem detail(String id) {
        return null;
    }
}
