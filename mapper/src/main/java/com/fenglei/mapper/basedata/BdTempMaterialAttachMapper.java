package com.fenglei.mapper.basedata;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenglei.model.basedata.BdTempMaterialAttach;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BdTempMaterialAttachMapper extends BaseMapper<BdTempMaterialAttach> {

    List<BdTempMaterialAttach> getList(@Param("bdTempMaterialAttach") BdTempMaterialAttach bdTempMaterialAttach);
}
