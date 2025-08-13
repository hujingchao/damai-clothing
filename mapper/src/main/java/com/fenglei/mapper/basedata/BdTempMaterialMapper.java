package com.fenglei.mapper.basedata;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.model.basedata.BdTempMaterial;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BdTempMaterialMapper extends BaseMapper<BdTempMaterial> {

    IPage<BdTempMaterial> getPage(Page page, @Param("bdTempMaterial") BdTempMaterial bdTempMaterial);

    List<BdTempMaterial> getList(@Param("bdTempMaterial") BdTempMaterial bdTempMaterial);

    BdTempMaterial infoById(String id);
}
