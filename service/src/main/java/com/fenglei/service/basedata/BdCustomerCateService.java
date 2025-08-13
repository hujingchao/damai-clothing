package com.fenglei.service.basedata;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.model.basedata.BdCustomerCate;

import java.util.List;

/**
 * 客户分类Service
 * Created by ljw on 2022/7/12.
 */
public interface BdCustomerCateService extends IService<BdCustomerCate> {

    IPage<BdCustomerCate> myPage(BdCustomerCate bdCustomerCate, Page page);

    List<BdCustomerCate> myList(BdCustomerCate bdCustomerCate);

    BdCustomerCate add(BdCustomerCate bdCustomerCate);

    BdCustomerCate myUpdate(BdCustomerCate bdCustomerCate);

    Boolean deleteById(Long id);

    BdCustomerCate detail(String id);

    void deleteBatch(String[] ids);

}
