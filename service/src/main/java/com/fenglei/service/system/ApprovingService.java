package com.fenglei.service.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.model.system.entity.Approving;

import java.util.List;

public interface ApprovingService extends IService<Approving> {

    IPage<Approving> myPage(Page page, Approving approving);

    List<Approving> myList(Approving approving);
}
