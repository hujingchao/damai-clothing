package com.fenglei.service.basedata;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.model.basedata.BdSpecification;

import java.util.List;

/**
 * @author ljw
 */
public interface BdSpecificationService extends IService<BdSpecification> {

    IPage<BdSpecification> myPage(Page page, BdSpecification bdSpecification);

    List<BdSpecification> myList(BdSpecification bdSpecification);

    BdSpecification add(BdSpecification bdSpecification) throws Exception;

    BdSpecification myUpdate(BdSpecification bdSpecification) throws Exception;

    Boolean deleteById(Long id);

    void deleteBatch(String[] ids);

    BdSpecification detail(String id);
}
