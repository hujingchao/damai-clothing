package com.fenglei.mapper.basedata;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.model.basedata.BdRepository;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BdRepositoryMapper extends BaseMapper<BdRepository> {

    IPage<BdRepository> getPage(Page page, @Param("bdRepository") BdRepository bdRepository);

    List<BdRepository> getList(@Param("bdRepository") BdRepository bdRepository);
}
