package com.fenglei.service.basedata;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.model.basedata.BdRepository;

import java.util.List;

/**
 * @author ljw
 */
public interface BdRepositoryService extends IService<BdRepository> {

    IPage<BdRepository> myPage(Page page, BdRepository bdRepository);

    List<BdRepository> myList(BdRepository bdRepository);

    BdRepository add(BdRepository bdRepository) throws Exception;

    BdRepository myUpdate(BdRepository bdRepository) throws Exception;

    Boolean deleteById(Long id);

    void deleteBatch(String[] ids);

    BdRepository detail(String id);
}
