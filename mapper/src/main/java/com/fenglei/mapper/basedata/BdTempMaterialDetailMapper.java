package com.fenglei.mapper.basedata;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenglei.model.basedata.BdTempMaterialDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BdTempMaterialDetailMapper extends BaseMapper<BdTempMaterialDetail> {

    List<BdTempMaterialDetail> getList(@Param("bdTempMaterialDetail") BdTempMaterialDetail bdTempMaterialDetail);

    List<BdTempMaterialDetail> getList2(@Param("bdTempMaterialDetail") BdTempMaterialDetail bdTempMaterialDetail);

    BdTempMaterialDetail infoById(String id);
}
