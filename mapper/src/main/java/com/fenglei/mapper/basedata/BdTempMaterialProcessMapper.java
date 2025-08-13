package com.fenglei.mapper.basedata;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenglei.model.basedata.BdTempMaterialProcess;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BdTempMaterialProcessMapper extends BaseMapper<BdTempMaterialProcess> {

    List<BdTempMaterialProcess> getList(@Param("bdTempMaterialProcess") BdTempMaterialProcess bdTempMaterialProcess);
}
