package com.fenglei.mapper.basedata;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenglei.model.basedata.BdTempWagesItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BdTempWagesItemMapper extends BaseMapper<BdTempWagesItem> {

    List<BdTempWagesItem> getList(@Param("bdTempWagesItem") BdTempWagesItem bdTempWagesItem);
}
