package com.fenglei.service.basedata;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.model.basedata.BdTag;

import java.util.List;

/**
 * @author ljw
 */
public interface BdTagService extends IService<BdTag> {

    IPage<BdTag> myPage(Page page, BdTag bdTag);

    List<BdTag> myList(BdTag bdTag);

    BdTag add(BdTag bdTag) throws Exception;

    BdTag myUpdate(BdTag bdTag) throws Exception;

    Boolean deleteById(Long id);

    void deleteBatch(String[] ids);

    BdTag detail(String id);
}
