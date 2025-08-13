package com.fenglei.mapper.prd;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.model.prd.dto.PrdMoDTO;
import com.fenglei.model.prd.entity.PrdMo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface PrdMoMapper extends BaseMapper<PrdMo> {

    IPage<PrdMo> getPage(Page page, @Param("prdMo") PrdMo prdMo);

    List<PrdMo> getList(@Param("prdMo") PrdMo prdMo);

    PrdMo infoById(String id);

    Integer getFinMtrlCount(@Param("prdMo") PrdMo prdMo);

    Integer getCount(@Param("prdMo") PrdMo prdMo);

    List<PrdMoDTO> getPrdMoDTO(@Param("prdMo") PrdMo prdMo);
}
