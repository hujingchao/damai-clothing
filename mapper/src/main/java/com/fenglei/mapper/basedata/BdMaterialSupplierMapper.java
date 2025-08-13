package com.fenglei.mapper.basedata;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenglei.model.basedata.BdMaterialSupplier;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BdMaterialSupplierMapper extends BaseMapper<BdMaterialSupplier> {

    List<BdMaterialSupplier> getList(@Param("bdMaterialSupplier") BdMaterialSupplier bdMaterialSupplier);
}
