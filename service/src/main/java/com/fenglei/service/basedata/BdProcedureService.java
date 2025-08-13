package com.fenglei.service.basedata;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.model.basedata.BdProcedure;

import java.util.List;

/**
 * @author ljw
 */
public interface BdProcedureService extends IService<BdProcedure> {

    IPage<BdProcedure> myPage(Page page, BdProcedure bdProcedure);

    List<BdProcedure> myList(BdProcedure bdProcedure);

    BdProcedure add(BdProcedure bdProcedure) throws Exception;

    BdProcedure myUpdate(BdProcedure bdProcedure) throws Exception;

    Boolean deleteById(Long id);

    void deleteBatch(String[] ids);

    BdProcedure detail(String id);
}
