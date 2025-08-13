package com.fenglei.mapper.basedata;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenglei.model.basedata.BdMaterialRaw;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BdMaterialRawMapper extends BaseMapper<BdMaterialRaw> {

    List<BdMaterialRaw> getList(@Param("bdMaterialRaw") BdMaterialRaw bdMaterialRaw);
}
