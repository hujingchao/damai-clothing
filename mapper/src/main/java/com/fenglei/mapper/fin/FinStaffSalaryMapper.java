package com.fenglei.mapper.fin;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.model.fin.dto.PieceRateFilter;
import com.fenglei.model.fin.vo.PieceRateVo;
import com.fenglei.model.prd.entity.PrdCutting;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface FinStaffSalaryMapper extends BaseMapper<PrdCutting> {

    IPage<PieceRateVo> searchPieceRates(Page<PieceRateVo> page, @Param("pieceRateFilter") PieceRateFilter pieceRateFilter);

    List<PieceRateVo> searchPieceRates(@Param("pieceRateFilter") PieceRateFilter pieceRateFilter);

    IPage<PieceRateVo> pagePieceRatesSummary(Page<PieceRateVo> page, @Param("pieceRateFilter") PieceRateFilter pieceRateFilter);

    BigDecimal getAllPieceRatesSummary(@Param("pieceRateFilter") PieceRateFilter pieceRateFilter);

    PieceRateVo getPieceRatesSummary(@Param("pieceRateFilter") PieceRateFilter pieceRateFilter);

    BigDecimal getTodayPieceRatesSummary(@Param("pieceRateFilter") PieceRateFilter pieceRateFilter);

    BigDecimal getLastMonthPieceRatesSummary(@Param("pieceRateFilter") PieceRateFilter pieceRateFilter);

    BigDecimal getCurrentMonthPieceRatesSummary(@Param("pieceRateFilter") PieceRateFilter pieceRateFilter);
}
