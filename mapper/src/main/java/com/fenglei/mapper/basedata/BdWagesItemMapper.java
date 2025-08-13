package com.fenglei.mapper.basedata;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenglei.model.basedata.BdWagesItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BdWagesItemMapper extends BaseMapper<BdWagesItem> {

    List<BdWagesItem> getList(@Param("bdWagesItem") BdWagesItem bdWagesItem);
}
