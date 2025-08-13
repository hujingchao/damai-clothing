package com.fenglei.mapper.basedata;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenglei.model.basedata.BdTempMaterialSupplier;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BdTempMaterialSupplierMapper extends BaseMapper<BdTempMaterialSupplier> {

    List<BdTempMaterialSupplier> getList(@Param("bdTempMaterialSupplier") BdTempMaterialSupplier bdTempMaterialSupplier);
}
