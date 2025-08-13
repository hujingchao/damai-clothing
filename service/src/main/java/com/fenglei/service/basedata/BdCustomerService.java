package com.fenglei.service.basedata;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.model.basedata.BdCustomer;

import java.util.List;

/**
 * @author ljw
 */
public interface BdCustomerService extends IService<BdCustomer> {

    IPage<BdCustomer> myPage(Page page, BdCustomer bdCustomer);

    List<BdCustomer> myList(BdCustomer bdCustomer);

    BdCustomer add(BdCustomer bdCustomer) throws Exception;

    BdCustomer myUpdate(BdCustomer bdCustomer) throws Exception;

    Boolean deleteById(Long id);

    void deleteBatch(String[] ids);

    BdCustomer detail(String id);
}
