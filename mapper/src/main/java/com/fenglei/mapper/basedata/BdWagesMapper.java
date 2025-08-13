package com.fenglei.mapper.basedata;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.model.basedata.BdWages;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BdWagesMapper extends BaseMapper<BdWages> {

    IPage<BdWages> getPage(Page page, @Param("bdWages") BdWages bdWages);

    IPage<BdWages> getPageWithTemp(Page page, @Param("bdWages") BdWages bdWages);

    BdWages infoById(String id);

    List<String> getNos(String noLike);
}
