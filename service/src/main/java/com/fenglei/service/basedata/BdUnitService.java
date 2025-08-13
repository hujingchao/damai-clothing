package com.fenglei.service.basedata;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.model.basedata.BdUnit;

import java.util.List;

/**
 * @author ljw
 */
public interface BdUnitService extends IService<BdUnit> {

    IPage<BdUnit> myPage(Page page, BdUnit bdUnit);

    List<BdUnit> myList(BdUnit bdUnit);

    BdUnit add(BdUnit bdUnit) throws Exception;

    BdUnit myUpdate(BdUnit bdUnit) throws Exception;

    Boolean deleteById(Long id);

    void deleteBatch(String[] ids);

    BdUnit detail(String id);
}
