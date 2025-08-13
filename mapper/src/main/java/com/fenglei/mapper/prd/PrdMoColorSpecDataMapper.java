package com.fenglei.mapper.prd;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenglei.model.prd.entity.PrdCuttingTicket;
import com.fenglei.model.prd.entity.PrdMo;
import com.fenglei.model.prd.entity.PrdMoColorSpecData;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface PrdMoColorSpecDataMapper extends BaseMapper<PrdMoColorSpecData> {

    List<PrdMoColorSpecData> getList(@Param("moColorSpecData") PrdMoColorSpecData prdMoColorSpecData);

    BigDecimal getAllQty(@Param("prdMo") PrdMo prdMo);

    BigDecimal getCuttingQty(@Param("prdMo") PrdMo prdMo);

    BigDecimal getInstockQty(@Param("prdMo") PrdMo prdMo);

    BigDecimal getAllInstockProcessAmt(@Param("prdMo") PrdMo prdMo);

    BigDecimal getAllIngProcessAmt(@Param("prdMo") PrdMo prdMo);
}
