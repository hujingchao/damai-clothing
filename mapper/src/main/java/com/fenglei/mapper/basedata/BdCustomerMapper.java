package com.fenglei.mapper.basedata;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.model.basedata.BdCustomer;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BdCustomerMapper extends BaseMapper<BdCustomer> {

    IPage<BdCustomer> getPage(Page page, @Param("bdCustomer") BdCustomer bdCustomer);

    List<BdCustomer> getList(@Param("bdCustomer") BdCustomer bdCustomer);

    BdCustomer infoById(String id);
}
