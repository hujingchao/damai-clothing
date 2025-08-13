package com.fenglei.service.prd.impl;

import com.fenglei.model.prd.entity.PrdInStockItem;
import com.fenglei.mapper.prd.PrdInStockItemMapper;
import com.fenglei.service.prd.IPrdInStockItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * <p>
 * 包装单分录 服务实现类
 * </p>
 *
 * @author zgm
 * @since 2024-04-18
 */
@Service
public class PrdInStockItemServiceImpl extends ServiceImpl<PrdInStockItemMapper, PrdInStockItem> implements IPrdInStockItemService {

    /**
     * 新增
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PrdInStockItem add(PrdInStockItem prdInStockItem) {
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
    public boolean myUpdate(PrdInStockItem prdInStockItem) {
        return true;
    }

    /**
     * 分页查询
     */
    @Override
    public IPage<PrdInStockItem> myPage(Page page, PrdInStockItem prdInStockItem) {
        return null;
    }

    /**
     * 详情
     */
    @Override
    public PrdInStockItem detail(String id) {
        return null;
    }
}
