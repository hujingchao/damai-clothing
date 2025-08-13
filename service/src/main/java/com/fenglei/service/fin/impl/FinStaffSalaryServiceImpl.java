package com.fenglei.service.fin.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.exception.BizException;
import com.fenglei.common.util.DateUtil;
import com.fenglei.common.util.ExcelUtils;
import com.fenglei.common.util.StringUtils;
import com.fenglei.mapper.fin.FinStaffSalaryMapper;
import com.fenglei.model.fin.dto.PieceRateFilter;
import com.fenglei.model.fin.dto.YearColumnsDto;
import com.fenglei.model.fin.vo.PieceRateVo;
import com.fenglei.model.fin.vo.StaffSalaryVo;
import com.fenglei.service.fin.IFinStaffSalaryService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * <p>
 * 员工工资 服务实现类
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-16
 */
@Service
public class FinStaffSalaryServiceImpl implements IFinStaffSalaryService {

    @Autowired
    FinStaffSalaryMapper staffSalaryMapper;

    @Override
    public IPage<PieceRateVo> pagePieceRates(Page<PieceRateVo> page, PieceRateFilter pieceRateFilter) {
        List<String> filterDateArr = pieceRateFilter.getFilterDateArr();
        if (StringUtils.isNotEmpty(filterDateArr)) {
            pieceRateFilter.setBeginDate(filterDateArr.get(0));
            pieceRateFilter.setEndDate(filterDateArr.get(1));
        }
        return staffSalaryMapper.searchPieceRates(page, pieceRateFilter);
    }

    @Override
    public Map<String, Object> listStaffSalary(PieceRateFilter pieceRateFilter) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");

        List<String> filterDateArr = pieceRateFilter.getFilterDateArr();
        if (StringUtils.isNotEmpty(filterDateArr)) {
            pieceRateFilter.setBeginDate(filterDateArr.get(0));
            pieceRateFilter.setEndDate(filterDateArr.get(1));
        } else {
            throw new BizException("请选择时间");
        }
        List<PieceRateVo> pieceRateVos = staffSalaryMapper.searchPieceRates(pieceRateFilter);
        List<StaffSalaryVo> staffSalaryVos = new ArrayList<>();
        for (PieceRateVo pieceRateVo : pieceRateVos) {
            StaffSalaryVo staffSalaryVo = staffSalaryVos.stream().filter(t -> t.getStaffId().equals(pieceRateVo.getStaffId())).findFirst().orElse(null);
            if (staffSalaryVo == null) {
                staffSalaryVo = new StaffSalaryVo();
                staffSalaryVo.setStaffId(pieceRateVo.getStaffId());
                staffSalaryVo.setStaffName(pieceRateVo.getStaffName());
                staffSalaryVo.setStaffNum(pieceRateVo.getStaffNum());
                staffSalaryVo.setDeptName(pieceRateVo.getDeptName());
                staffSalaryVo.setBasicSalary(BigDecimal.ZERO); // todo 暂时无基本工资
                staffSalaryVo.setAllSalary(BigDecimal.ZERO);
                staffSalaryVo.setPieceRates(new HashMap<>());
                staffSalaryVos.add(staffSalaryVo);
            }
            staffSalaryVo.setAllSalary(staffSalaryVo.getAllSalary().add(pieceRateVo.getAmount()));

            String reportTime = pieceRateVo.getReportTime();
            Date parse = sdf.parse(reportTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parse);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            String key = year + "-" + month;
            Map<String, BigDecimal> pieceRates = staffSalaryVo.getPieceRates();
            if (!pieceRates.containsKey(key)) {
                pieceRates.put(key, pieceRateVo.getAmount());
            } else {
                pieceRates.put(key, pieceRateVo.getAmount().add(pieceRates.get(key)));
            }
        }

        String beginDate = pieceRateFilter.getBeginDate();
        String endDate = pieceRateFilter.getEndDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf3.parse(beginDate));
        int beginYear = calendar.get(Calendar.YEAR);
        int beginMonth = calendar.get(Calendar.MONTH) + 1;

        calendar.setTime(sdf3.parse(endDate));
        int endYear = calendar.get(Calendar.YEAR);
        int endMonth = calendar.get(Calendar.MONTH) + 1;
        List<YearColumnsDto> columns = new ArrayList<>();
        for (int i = beginYear; i < endYear + 1; i++) {
            YearColumnsDto dto = new YearColumnsDto();
            dto.setYear(i);
            dto.setMonth(new ArrayList<>());
            int firstMonth = beginMonth;
            if (i != beginYear) {
                firstMonth = 1;
            }

            int lastMonth = endMonth;
            if (beginYear != endYear && i != endYear) {
                lastMonth = 12;
            }
            for (int j = firstMonth; j < lastMonth + 1; j++) {
                dto.getMonth().add(j);
            }

            columns.add(dto);
        }

        HashMap<String, Object> result = new HashMap<>();
        result.put("columns", columns);
        result.put("data", staffSalaryVos);
        return result;
    }

    @Override
    public void exportPieceRate(HttpServletResponse response, PieceRateFilter pieceRateFilter) {
        List<String> filterDateArr = pieceRateFilter.getFilterDateArr();
        if (StringUtils.isNotEmpty(filterDateArr)) {
            pieceRateFilter.setBeginDate(filterDateArr.get(0));
            pieceRateFilter.setEndDate(filterDateArr.get(1));
        }
        List<PieceRateVo> pieceRateVos = staffSalaryMapper.searchPieceRates(pieceRateFilter);
        // 创建一个excel
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 新建sheet页
        HSSFSheet sheet = workbook.createSheet("sheet1");
        //设置默认宽度好像必须先设置默认高度，不然不生效。。。。。。
        sheet.setDefaultRowHeight((short) (1 * 256));
        sheet.setDefaultColumnWidth(20);


        HSSFCellStyle cellStyle = workbook.createCellStyle();
        HSSFFont fontStyle = workbook.createFont();
        fontStyle.setBold(true);
        fontStyle.setFontHeightInPoints((short) 12);
        cellStyle.setFont(fontStyle);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置边框
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);

        HSSFCellStyle cellStyleContent = workbook.createCellStyle();
        cellStyleContent.setAlignment(HorizontalAlignment.CENTER);
        cellStyleContent.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置边框
        cellStyleContent.setBorderTop(BorderStyle.THIN);
        cellStyleContent.setBorderBottom(BorderStyle.THIN);
        cellStyleContent.setBorderLeft(BorderStyle.THIN);
        cellStyleContent.setBorderRight(BorderStyle.THIN);
        HSSFFont fontStyleContent = workbook.createFont();
        fontStyle.setFontHeightInPoints((short) 12);
        cellStyleContent.setFont(fontStyleContent);


        //第一行   头
        HSSFRow row2 = sheet.createRow(sheet.getLastRowNum() + 1);
        row2.setHeightInPoints(20);
        List<String> title = new ArrayList<>();
        title.add("序号");
        title.add("员工编码");
        title.add("员工名称");
        title.add("部门");
        title.add("汇报日期");
        title.add("工序");
        title.add("工价");
        title.add("上数汇报数量");
        title.add("数量");
        title.add("是否收货");
        title.add("工资");
        title.add("商家编码");
        title.add("名称");
        title.add("颜色");
        title.add("规格");
        for (int i = 0; i < title.size(); i++) {
            HSSFCell cel = row2.createCell(i);
            cel.setCellStyle(cellStyle);
            cel.setCellValue(title.get(i));
        }
        // 数据
        for (int j = 0, recordsSize = pieceRateVos.size(); j < recordsSize; j++) {
            PieceRateVo record = pieceRateVos.get(j);
            HSSFRow rowData = sheet.createRow(sheet.getLastRowNum() + 1);
            rowData.setHeightInPoints(15);
            rowData.createCell(0).setCellValue(j + 1);
            rowData.createCell(1).setCellValue(record.getStaffNum());
            rowData.createCell(2).setCellValue(record.getStaffName());
            rowData.createCell(3).setCellValue(record.getDeptName());
            rowData.createCell(4).setCellValue(record.getReportTime());
            rowData.createCell(5).setCellValue(record.getProcedureName());
            rowData.createCell(6).setCellValue(String.valueOf(record.getPrice()));
            rowData.createCell(7).setCellValue(String.valueOf(record.getRealReportedQty()));
            rowData.createCell(8).setCellValue(String.valueOf(record.getQty()));
            rowData.createCell(9).setCellValue(record.getIsInStock()?"是":"否");
            rowData.createCell(10).setCellValue(String.valueOf(record.getAmount()));
            rowData.createCell(11).setCellValue(record.getNumber());
            rowData.createCell(12).setCellValue(record.getProductName());
            rowData.createCell(13).setCellValue(record.getColor());
            rowData.createCell(14).setCellValue(record.getSpecification());
            rowData.getCell(0).setCellStyle(cellStyleContent);
            rowData.getCell(1).setCellStyle(cellStyleContent);
            rowData.getCell(2).setCellStyle(cellStyleContent);
            rowData.getCell(3).setCellStyle(cellStyleContent);
            rowData.getCell(4).setCellStyle(cellStyleContent);
            rowData.getCell(5).setCellStyle(cellStyleContent);
            rowData.getCell(6).setCellStyle(cellStyleContent);
            rowData.getCell(7).setCellStyle(cellStyleContent);
            rowData.getCell(8).setCellStyle(cellStyleContent);
            rowData.getCell(9).setCellStyle(cellStyleContent);
            rowData.getCell(10).setCellStyle(cellStyleContent);
            rowData.getCell(11).setCellStyle(cellStyleContent);
            rowData.getCell(12).setCellStyle(cellStyleContent);
            rowData.getCell(13).setCellStyle(cellStyleContent);
            rowData.getCell(14).setCellStyle(cellStyleContent);
        }
        ExcelUtils.buildXlsxDocument("计件工资表", workbook, response);
    }

    @Override
    public void exportStaffSalary(HttpServletResponse response, PieceRateFilter pieceRateFilter) throws ParseException {
        Map<String, Object> staffSalary = this.listStaffSalary(pieceRateFilter);
        List<YearColumnsDto> columns = (List<YearColumnsDto>) staffSalary.get("columns");
        List<StaffSalaryVo> salaryVos = (List<StaffSalaryVo>) staffSalary.get("data");
        // 创建一个excel
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 新建sheet页
        HSSFSheet sheet = workbook.createSheet("sheet1");
        //设置默认宽度好像必须先设置默认高度，不然不生效。。。。。。
        sheet.setDefaultRowHeight((short) (1 * 256));
        sheet.setDefaultColumnWidth(20);

        HSSFCellStyle headStyle = workbook.createCellStyle();
        HSSFFont fontStyle = workbook.createFont();
        fontStyle.setBold(true);
        fontStyle.setFontHeightInPoints((short) 12);
        headStyle.setFont(fontStyle);
        headStyle.setAlignment(HorizontalAlignment.CENTER);
        headStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置边框
        headStyle.setBorderTop(BorderStyle.THIN);
        headStyle.setBorderBottom(BorderStyle.THIN);
        headStyle.setBorderLeft(BorderStyle.THIN);
        headStyle.setBorderRight(BorderStyle.THIN);

        List<String> title = new ArrayList<>();
        title.add("序号");
        title.add("员工编码");
        title.add("员工名称");
        title.add("部门");
        int allCMonthCount = 0;
        //第一行   头
        HSSFRow row1 = sheet.createRow(sheet.getLastRowNum() + 1);
        row1.setHeightInPoints(20);
        for (int i = 0; i < title.size(); i++) {
            HSSFCell cel = row1.createCell(i);
            cel.setCellStyle(headStyle);
            cel.setCellValue(title.get(i));
        }
        for (YearColumnsDto column : columns) {
            int index = 4;
            List<Integer> month = column.getMonth();
            for (Integer m : month) {
                allCMonthCount++;
                HSSFCell cel = row1.createCell(index++);
                cel.setCellStyle(headStyle);
                cel.setCellValue(column.getYear() + "年");
            }
            HSSFCell cel = row1.createCell(index);
            cel.setCellStyle(headStyle);
            cel.setCellValue("总工资");
        }
        //第二行   头
        HSSFRow row2 = sheet.createRow(sheet.getLastRowNum() + 1);
        row2.setHeightInPoints(20);
        for (int i = 0; i < title.size(); i++) {
            HSSFCell cel = row2.createCell(i);
            cel.setCellStyle(headStyle);
            cel.setCellValue(title.get(i));
        }
        for (YearColumnsDto column : columns) {
            int index = 4;
            List<Integer> month = column.getMonth();
            for (Integer m : month) {
                HSSFCell cel = row2.createCell(index++);
                cel.setCellStyle(headStyle);
                cel.setCellValue(m);
            }
            HSSFCell cel = row2.createCell(index);
            cel.setCellStyle(headStyle);
            cel.setCellValue("总工资");

            if  (index - 1 > 4) {
                CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, 4, index - 1);
                sheet.addMergedRegion(cellRangeAddress);
            }
        }

        for (int i = 0; i < title.size(); i++) {
            CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 1, i, i);
            sheet.addMergedRegion(cellRangeAddress);
        }
        CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 1, allCMonthCount + 4, allCMonthCount + 4);
        sheet.addMergedRegion(cellRangeAddress);


        // 数据
        HSSFCellStyle cellStyleContent = workbook.createCellStyle();
        cellStyleContent.setAlignment(HorizontalAlignment.CENTER);
        cellStyleContent.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置边框
        cellStyleContent.setBorderTop(BorderStyle.THIN);
        cellStyleContent.setBorderBottom(BorderStyle.THIN);
        cellStyleContent.setBorderLeft(BorderStyle.THIN);
        cellStyleContent.setBorderRight(BorderStyle.THIN);
        Map<String, BigDecimal> footerData = new HashMap<>();
        for (int i = 0; i < salaryVos.size(); i++) {
            StaffSalaryVo salaryVo = salaryVos.get(i);
            Map<String, BigDecimal> pieceRates = salaryVo.getPieceRates();
            HSSFRow rowData = sheet.createRow(sheet.getLastRowNum() + 1);
            rowData.setHeightInPoints(15);
            rowData.createCell(0).setCellValue(i + 1);
            rowData.createCell(1).setCellValue(salaryVo.getStaffNum());
            rowData.createCell(2).setCellValue(salaryVo.getStaffName());
            rowData.createCell(3).setCellValue(salaryVo.getDeptName());
            rowData.getCell(0).setCellStyle(cellStyleContent);
            rowData.getCell(1).setCellStyle(cellStyleContent);
            rowData.getCell(2).setCellStyle(cellStyleContent);
            rowData.getCell(3).setCellStyle(cellStyleContent);
            for (YearColumnsDto column : columns) {
                int index = 4;
                List<Integer> month = column.getMonth();
                for (Integer m : month) {
                    HSSFCell cell = rowData.createCell(index++);
                    String key = column.getYear() + "-" + m;
                    BigDecimal cur = pieceRates.get(key) == null ? BigDecimal.ZERO : pieceRates.get(key);
                    cell.setCellValue(cur.doubleValue());
                    cell.setCellStyle(cellStyleContent);

                    footerData.merge(key, cur, BigDecimal::add);
                }
            }
            HSSFCell cel = rowData.createCell(allCMonthCount + 4);
            cel.setCellStyle(cellStyleContent);
            cel.setCellValue(salaryVo.getAllSalary() == null ? 0 : salaryVo.getAllSalary().doubleValue());
        }

        // 表尾合计
        HSSFRow rowFooter = sheet.createRow(sheet.getLastRowNum() + 1);
        rowFooter.setHeightInPoints(20);
        for (int i = 0; i < 4; i++) {
            HSSFCell cell = rowFooter.createCell(i);
            cell.setCellValue("合计");
            cell.setCellStyle(headStyle);
        }
        BigDecimal allAmount = BigDecimal.ZERO;
        for (YearColumnsDto column : columns) {
            int index = 4;
            List<Integer> month = column.getMonth();
            for (Integer m : month) {
                HSSFCell cell = rowFooter.createCell(index++);
                String key = column.getYear() + "-" + m;
                BigDecimal cur = footerData.get(key);
                allAmount = allAmount.add(cur);
                cell.setCellValue(cur.doubleValue());
                cell.setCellStyle(headStyle);
            }
        }
        HSSFCell cel = rowFooter.createCell(allCMonthCount + 4);
        cel.setCellStyle(cellStyleContent);
        cel.setCellValue(allAmount.doubleValue());

        ExcelUtils.buildXlsxDocument("工资管理", workbook, response);
    }

    @Override
    public IPage<PieceRateVo> pagePieceRatesSummary(Page<PieceRateVo> page, PieceRateFilter pieceRateFilter) {
        List<String> filterDateArr = pieceRateFilter.getFilterDateArr();
        if (StringUtils.isNotEmpty(filterDateArr)) {
            pieceRateFilter.setBeginDate(filterDateArr.get(0));
            pieceRateFilter.setEndDate(filterDateArr.get(1));
        }
        return staffSalaryMapper.pagePieceRatesSummary(page, pieceRateFilter);
    }

    @Override
    public BigDecimal getAllPieceRatesSummary(PieceRateFilter pieceRateFilter) {
        List<String> filterDateArr = pieceRateFilter.getFilterDateArr();
        if (StringUtils.isNotEmpty(filterDateArr)) {
            pieceRateFilter.setBeginDate(filterDateArr.get(0));
            pieceRateFilter.setEndDate(filterDateArr.get(1));
        }
        return staffSalaryMapper.getAllPieceRatesSummary(pieceRateFilter);
    }

    @Override
    public PieceRateVo getPieceRatesSummary(PieceRateFilter pieceRateFilter) {
        pieceRateFilter.setCurrentMonthBeginDate(DateUtil.getMonthFirstDay());
        pieceRateFilter.setCurrentMonthEndDate(DateUtil.getMonthEndDay());
        pieceRateFilter.setLastMonthBeginDate(DateUtil.getLastMonthStartDay());
        pieceRateFilter.setLastMonthEndDate(DateUtil.getLastMonthEndDay());
        PieceRateVo pieceRateVo = new PieceRateVo();
        pieceRateVo.setTodayAmount(staffSalaryMapper.getTodayPieceRatesSummary(pieceRateFilter));
        pieceRateVo.setLastMonthAmount(staffSalaryMapper.getLastMonthPieceRatesSummary(pieceRateFilter));
        pieceRateVo.setCurrentMonthAmount(staffSalaryMapper.getCurrentMonthPieceRatesSummary(pieceRateFilter));
        return pieceRateVo;
    }
}
