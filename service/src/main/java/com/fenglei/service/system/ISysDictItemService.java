package com.fenglei.service.system;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.model.system.entity.SysDictItem;

import java.util.Collection;
import java.util.List;


public interface ISysDictItemService extends IService<SysDictItem> {

    IPage<SysDictItem> list(Page<SysDictItem> page, SysDictItem dict);

    List<SysDictItem> listByLabels(String dictCode, Collection<String> labels);
}
