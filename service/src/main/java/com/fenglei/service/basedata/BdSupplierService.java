package com.fenglei.service.basedata;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.model.basedata.BdSupplier;

import java.util.List;

/**
 * @author ljw
 */
public interface BdSupplierService extends IService<BdSupplier> {

    IPage<BdSupplier> myPage(Page page, BdSupplier bdSupplier);

    List<BdSupplier> myList(BdSupplier bdSupplier);

    BdSupplier add(BdSupplier bdSupplier) throws Exception;

    BdSupplier myUpdate(BdSupplier bdSupplier) throws Exception;

    Boolean deleteById(Long id);

    void deleteBatch(String[] ids);

    BdSupplier detail(String id);
}
