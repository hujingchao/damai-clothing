package com.fenglei.service.system.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.mapper.system.SysDictItemMapper;
import com.fenglei.model.system.entity.SysDictItem;
import com.fenglei.service.system.ISysDictItemService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class SysDictItemServiceImpl extends ServiceImpl<SysDictItemMapper, SysDictItem> implements ISysDictItemService {

    @Override
    public IPage<SysDictItem> list(Page<SysDictItem> page, SysDictItem dict) {
        List<SysDictItem> list = this.baseMapper.list(page, dict);
        page.setRecords(list);
        return page;
    }

    @Override
    public List<SysDictItem> listByLabels(String dictCode, Collection<String> labels) {
        return this.list(Wrappers.lambdaQuery(SysDictItem.class).eq(SysDictItem::getDictCode, dictCode).in(SysDictItem::getName, labels));
    }

}
