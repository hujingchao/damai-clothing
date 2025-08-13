package com.fenglei.service.basedata;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.model.basedata.BdColor;

import java.util.List;

/**
 * @author ljw
 */
public interface BdColorService extends IService<BdColor> {

    IPage<BdColor> myPage(Page page, BdColor bdColor);

    List<BdColor> myList(BdColor bdColor);

    BdColor add(BdColor bdColor) throws Exception;

    BdColor myUpdate(BdColor bdColor) throws Exception;

    Boolean deleteById(Long id);

    void deleteBatch(String[] ids);

    BdColor detail(String id);
}
