package com.fenglei.admin.controller.fin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.result.Result;
import com.fenglei.model.fin.dto.PieceRateFilter;
import com.fenglei.model.fin.vo.PieceRateVo;
import com.fenglei.service.fin.IFinStaffSalaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Map;

/**
 * <p>
 * 员工工资 前端控制器
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-02
 */
@Api(tags = "员工工资 前端控制器")
@RestController
@RequestMapping("/api.admin/v1/finStaffSalary")
@AllArgsConstructor
@Slf4j
public class FinStaffSalaryController {

    IFinStaffSalaryService staffSalaryService;

    @ApiOperation(value = "计件工资查询")
    @PostMapping("/pagePieceRatesSummary")
    public Result<IPage<PieceRateVo>> pagePieceRatesSummary(Page<PieceRateVo> page, @RequestBody PieceRateFilter pieceRateFilter) {
        IPage<PieceRateVo> result = staffSalaryService.pagePieceRatesSummary(page, pieceRateFilter);
        return Result.success(result);
    }

    @ApiOperation(value = "计件工资总金额查询")
    @PostMapping("/getAllPieceRatesSummary")
    public Result<BigDecimal> getAllPieceRatesSummary(@RequestBody PieceRateFilter pieceRateFilter) {
        BigDecimal result = staffSalaryService.getAllPieceRatesSummary(pieceRateFilter);
        return Result.success(result);
    }

    @ApiOperation(value = "计件工资查询")
    @PostMapping("/getPieceRatesSummary")
    public Result<PieceRateVo> getPieceRatesSummary(@RequestBody PieceRateFilter pieceRateFilter) {
        PieceRateVo result = staffSalaryService.getPieceRatesSummary(pieceRateFilter);
        return Result.success(result);
    }

    @ApiOperation(value = "计件工资查询")
    @PostMapping("/pagePieceRates")
    public Result<IPage<PieceRateVo>> pagePieceRates(Page<PieceRateVo> page, @RequestBody PieceRateFilter pieceRateFilter) {
        IPage<PieceRateVo> result = staffSalaryService.pagePieceRates(page, pieceRateFilter);
        return Result.success(result);
    }

    @ApiOperation(value = "工资管理")
    @PostMapping("/listStaffSalary")
    public Result<Map<String, Object>> listStaffSalary(@RequestBody PieceRateFilter pieceRateFilter) throws ParseException {
        Map<String, Object> result = staffSalaryService.listStaffSalary(pieceRateFilter);
        return Result.success(result);
    }

    @ApiOperation(value = "计件工资导出")
    @PostMapping("/exportPieceRate")
    public void exportPieceRate(HttpServletResponse response, @RequestBody PieceRateFilter pieceRateFilter) throws Exception {
        staffSalaryService.exportPieceRate(response, pieceRateFilter);
    }

    @ApiOperation(value = "工资管理导出")
    @PostMapping("/exportStaffSalary")
    public void exportStaffSalary(HttpServletResponse response, @RequestBody PieceRateFilter pieceRateFilter) throws Exception {
        staffSalaryService.exportStaffSalary(response, pieceRateFilter);
    }
}
