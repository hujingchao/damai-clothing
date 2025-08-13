package com.fenglei.mapper.basedata;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenglei.model.basedata.BdMaterialAux;
import com.fenglei.model.basedata.BdMaterialDetail;
import com.fenglei.model.basedata.dto.MaterialDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BdMaterialDetailMapper extends BaseMapper<BdMaterialDetail> {

    List<BdMaterialDetail> getList(@Param("bdMaterialDetail") BdMaterialDetail bdMaterialDetail);

    List<BdMaterialDetail> getList2(@Param("bdMaterialDetail") BdMaterialDetail bdMaterialDetail);

    List<MaterialDTO> getMaterialDTOByDetailIds(@Param("materialDetailIds") List<String> materialDetailIds);

    List<String> getNos(String noLike);

    BdMaterialDetail infoById(String id);
}
