package com.fenglei.mapper.basedata;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenglei.model.basedata.BdMaterialAttach;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BdMaterialAttachMapper extends BaseMapper<BdMaterialAttach> {

    List<BdMaterialAttach> getList(@Param("bdMaterialAttach") BdMaterialAttach bdMaterialAttach);
}
