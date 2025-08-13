package com.fenglei.mapper.basedata;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.model.basedata.BdTempWages;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BdTempWagesMapper extends BaseMapper<BdTempWages> {

    IPage<BdTempWages> getPage(Page page, @Param("bdTempWages") BdTempWages bdTempWages);

    List<BdTempWages> getList(@Param("bdTempWages") BdTempWages bdTempWages);

    BdTempWages infoById(String id);
}
