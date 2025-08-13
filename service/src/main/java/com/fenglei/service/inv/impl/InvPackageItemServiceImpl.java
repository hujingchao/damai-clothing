package com.fenglei.service.inv.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fenglei.model.inv.entity.InvPackageItem;
import com.fenglei.mapper.inv.InvPackageItemMapper;
import com.fenglei.model.inv.entity.InvPackageNo;
import com.fenglei.service.inv.IInvPackageItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 入库后打包子表 服务实现类
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-23
 */
@Service
public class InvPackageItemServiceImpl extends ServiceImpl<InvPackageItemMapper, InvPackageItem> implements IInvPackageItemService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<InvPackageItem> batchAdd(List<InvPackageItem> packageItems) {
        this.saveBatch(packageItems);
        return packageItems;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeByPid(String pid) {
        return this.remove(Wrappers.lambdaQuery(InvPackageItem.class).eq(InvPackageItem::getPid, pid));
    }

    @Override
    public List<InvPackageItem> listByPid(String pid) {
        List<InvPackageItem> packageItems = this.baseMapper.listByPid(pid);
        return packageItems;
    }
}
