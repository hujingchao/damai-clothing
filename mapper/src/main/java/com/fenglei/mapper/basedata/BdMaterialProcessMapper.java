package com.fenglei.mapper.basedata;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenglei.model.basedata.BdMaterialProcess;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BdMaterialProcessMapper extends BaseMapper<BdMaterialProcess> {

    List<BdMaterialProcess> getList(@Param("bdMaterialProcess") BdMaterialProcess bdMaterialProcess);
}
