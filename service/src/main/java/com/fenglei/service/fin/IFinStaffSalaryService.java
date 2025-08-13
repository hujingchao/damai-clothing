package com.fenglei.service.fin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.model.fin.dto.PieceRateFilter;
import com.fenglei.model.fin.vo.PieceRateVo;
import com.fenglei.model.fin.vo.StaffSalaryVo;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 员工工资 服务类
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-16
 */
public interface IFinStaffSalaryService {

    /**
     * 计件工资查询
     * @param page 分页参数
     * @param pieceRateFilter 过滤条件
     */
    IPage<PieceRateVo> pagePieceRates(Page<PieceRateVo> page, PieceRateFilter pieceRateFilter);

    /**
     * 员工工资
     * @param pieceRateFilter 过滤条件
     */
    Map<String, Object> listStaffSalary(PieceRateFilter pieceRateFilter) throws ParseException;

    void exportPieceRate(HttpServletResponse response, PieceRateFilter pieceRateFilter);

    void exportStaffSalary(HttpServletResponse response, PieceRateFilter pieceRateFilter) throws ParseException;

    IPage<PieceRateVo> pagePieceRatesSummary(Page<PieceRateVo> page, PieceRateFilter pieceRateFilter);

    BigDecimal getAllPieceRatesSummary(PieceRateFilter pieceRateFilter);

    PieceRateVo getPieceRatesSummary(PieceRateFilter pieceRateFilter);
}
