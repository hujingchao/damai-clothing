package com.fenglei.service.prd.impl;

import com.fenglei.model.prd.entity.PrdPackingItem;
import com.fenglei.mapper.prd.PrdPackingItemMapper;
import com.fenglei.service.prd.IPrdPackingItemService;
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
 * @since 2024-04-11
 */
@Service
public class PrdPackingItemServiceImpl extends ServiceImpl<PrdPackingItemMapper, PrdPackingItem> implements IPrdPackingItemService {

    /**
     * 新增
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PrdPackingItem add(PrdPackingItem prdPackingItem) {
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
    public boolean myUpdate(PrdPackingItem prdPackingItem) {
        return true;
    }

    /**
     * 分页查询
     */
    @Override
    public IPage<PrdPackingItem> myPage(Page page, PrdPackingItem prdPackingItem) {
        return null;
    }

    /**
     * 详情
     */
    @Override
    public PrdPackingItem detail(String id) {
        return null;
    }
}
