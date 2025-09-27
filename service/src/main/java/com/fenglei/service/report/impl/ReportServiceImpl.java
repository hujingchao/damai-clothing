package com.fenglei.service.report.impl;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.datatype.jsr310.DecimalUtils;
import com.fenglei.common.exception.BizException;
import com.fenglei.common.util.ExcelUtils;
import com.fenglei.common.util.StringUtils;
import com.fenglei.common.util.im.ExcelUtil;
import com.fenglei.mapper.report.ReportMapper;
import com.fenglei.model.pur.entity.PurPurchaseInstockItem;
import com.fenglei.model.pur.vo.PurPurchaseInstockItemVo;
import com.fenglei.model.report.dto.CuttingTicketReportDto;
import com.fenglei.model.report.dto.MaterialsReportDto;
import com.fenglei.model.report.vo.CuttingTicketReportVo;
import com.fenglei.model.report.vo.MaterialsReportItemVo;
import com.fenglei.model.report.vo.MaterialsReportVo;
import com.fenglei.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportMapper reportMapper;


    @Override
    public IPage<MaterialsReportVo> materialsReportPage(Page<MaterialsReportVo> page, MaterialsReportDto reportDto) {
        if (StringUtils.isEmpty(reportDto.getMonth())) {
            throw new BizException("请选择要查询的日期！");
        }
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate searchDate =
                LocalDate.parse(reportDto.getMonth() + "-01", dayFormatter);
        // 获取当前月的第一天
        LocalDate firstDay = searchDate.with(TemporalAdjusters.firstDayOfMonth());
        // 获取当前月的最后一天
        LocalDate lastDay = searchDate.with(TemporalAdjusters.lastDayOfMonth());
        List<LocalDate> rangeDays = getRangeDays(firstDay, lastDay);

        IPage<MaterialsReportVo> result = reportMapper.materialsReport(page, reportDto);
        //查询详情
        List<PurPurchaseInstockItem> instockItemList = reportMapper.getPurchaseInstockItemList(reportDto);

        for (MaterialsReportVo record : result.getRecords()) {
            List<PurPurchaseInstockItem> itemList = instockItemList.stream().filter(s -> s.getMaterialDetailId().equals(record.getMaterialDetailId())).collect(Collectors.toList());
            List<MaterialsReportItemVo> itemVoList = new ArrayList<>();
            BigDecimal totalQty = BigDecimal.ZERO;
            BigDecimal totalPiQty = BigDecimal.ZERO;
            for (LocalDate rangeDay : rangeDays) {
                String bizDate = rangeDay.format(dayFormatter);
                List<PurPurchaseInstockItem> collect = itemList.stream().filter(s -> StringUtils.isNotEmpty(s.getBizDate()) && s.getBizDate().equals(bizDate)).collect(Collectors.toList());
                BigDecimal qty = BigDecimal.ZERO;
                BigDecimal piQty = BigDecimal.ZERO;
                for (PurPurchaseInstockItem purPurchaseInstockItem : collect) {
                    qty = qty.add(purPurchaseInstockItem.getQty());
                    piQty = piQty.add(purPurchaseInstockItem.getPiQty());
                }
                MaterialsReportItemVo materialsReportItemVo = new MaterialsReportItemVo();
                materialsReportItemVo.setMaterialDetailId(record.getMaterialDetailId());
                materialsReportItemVo.setBizDate(bizDate);
                materialsReportItemVo.setQty(qty);
                materialsReportItemVo.setPiQty(piQty);
                totalQty = totalQty.add(qty);
                totalPiQty = totalPiQty.add(piQty);
                itemVoList.add(materialsReportItemVo);
            }
            record.setItemList(itemVoList);
            record.setTotalQty(totalQty);
            record.setTotalPiQty(totalPiQty);
        }
        return result;
    }

    @Override
    public void materialsReportExport(HttpServletResponse response, MaterialsReportDto reportDto) {
        if (StringUtils.isEmpty(reportDto.getMonth())) {
            throw new BizException("请选择要导出的月份！");
        }
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate searchDate =
                LocalDate.parse(reportDto.getMonth() + "-01", dayFormatter);
        // 获取当前月的第一天
        LocalDate firstDay = searchDate.with(TemporalAdjusters.firstDayOfMonth());
        // 获取当前月的最后一天
        LocalDate lastDay = searchDate.with(TemporalAdjusters.lastDayOfMonth());
        List<LocalDate> rangeDays = getRangeDays(firstDay, lastDay);

        List<MaterialsReportVo> result = reportMapper.materialsReport(reportDto);
        //查询详情
        List<PurPurchaseInstockItem> instockItemList = reportMapper.getPurchaseInstockItemList(reportDto);

        for (MaterialsReportVo record : result) {
            List<PurPurchaseInstockItem> itemList = instockItemList.stream().filter(s -> s.getMaterialDetailId().equals(record.getMaterialDetailId())).collect(Collectors.toList());
            List<MaterialsReportItemVo> itemVoList = new ArrayList<>();
            BigDecimal totalQty = BigDecimal.ZERO;
            BigDecimal totalPiQty = BigDecimal.ZERO;
            for (LocalDate rangeDay : rangeDays) {
                String bizDate = rangeDay.format(dayFormatter);
                List<PurPurchaseInstockItem> collect = itemList.stream().filter(s -> StringUtils.isNotEmpty(s.getBizDate()) && s.getBizDate().equals(bizDate)).collect(Collectors.toList());
                BigDecimal qty = BigDecimal.ZERO;
                BigDecimal piQty = BigDecimal.ZERO;
                for (PurPurchaseInstockItem purPurchaseInstockItem : collect) {
                    qty = qty.add(purPurchaseInstockItem.getQty());
                    piQty = piQty.add(purPurchaseInstockItem.getPiQty());
                }
                MaterialsReportItemVo materialsReportItemVo = new MaterialsReportItemVo();
                materialsReportItemVo.setMaterialDetailId(record.getMaterialDetailId());
                materialsReportItemVo.setBizDate(bizDate);
                materialsReportItemVo.setQty(qty);
                materialsReportItemVo.setPiQty(piQty);
                totalQty = totalQty.add(qty);
                totalPiQty = totalPiQty.add(piQty);
                itemVoList.add(materialsReportItemVo);
            }
            record.setItemList(itemVoList);
            record.setTotalQty(totalQty);
            record.setTotalPiQty(totalPiQty);
        }


        //获取表头
        List<Map<String, String>> headMapList = getHeadMapList(reportDto.getSelectType(), result, rangeDays);

        //获取表格map
        List<Map<String, Object>> mapList = getMapList(reportDto.getSelectType(), result, rangeDays);


        ExcelUtils.writeExcelMap(headMapList, mapList, "来料汇总表", "sheet1", response);

    }

    @Override
    public IPage<CuttingTicketReportVo> cuttingTicketReportPage(Page<CuttingTicketReportVo> page, CuttingTicketReportDto reportDto) {
        //生产订单限制了一单无法有相同的skuid 可以这么查询 left join prd_cutting_raw pcr on pct.sku_id = pcr.parent_sku_id and pct.pid = pcr.pid
        IPage<CuttingTicketReportVo> result = reportMapper.cuttingTicketReport(page, reportDto);

        if(!result.getRecords().isEmpty()){
            for (CuttingTicketReportVo record : result.getRecords()) {
                record.setUsedQty(record.getRealQty().divide(record.getSumQty(),3, RoundingMode.HALF_UP));
                record.setStandardQty(record.getTheoryQty().divide(record.getSumQty(),3, RoundingMode.HALF_UP));
                if(record.getUsedQty().compareTo(BigDecimal.ZERO) !=0 && !(record.getStandardQty().compareTo(BigDecimal.ZERO) == 0)){
                    record.setError(record.getUsedQty().subtract(record.getStandardQty()));
                    record.setErrorRate(record.getError().multiply(new BigDecimal("100")).divide(record.getStandardQty(),3, RoundingMode.HALF_UP));
                }
            }
        }
        return result;
    }

    @Override
    public void cuttingTicketReportExport(HttpServletResponse response, CuttingTicketReportDto reportDto) {
        List<CuttingTicketReportVo> list = reportMapper.cuttingTicketReport(reportDto);
        for (CuttingTicketReportVo reportVo :list) {
            reportVo.setUsedQty(reportVo.getRealQty().divide(reportVo.getSumQty(),3, RoundingMode.HALF_UP));
            reportVo.setStandardQty(reportVo.getTheoryQty().divide(reportVo.getSumQty(),3, RoundingMode.HALF_UP));
            if(reportVo.getUsedQty().compareTo(BigDecimal.ZERO) !=0 ){
                reportVo.setError(reportVo.getUsedQty().subtract(reportVo.getStandardQty()));
                reportVo.setErrorRate(reportVo.getError().divide(reportVo.getUsedQty(),3, RoundingMode.HALF_UP));
            }
        }

        ExcelUtils.writeExcel(response, list, CuttingTicketReportVo.class, "裁床统计报表导出.xlsx");
    }

    private List<Map<String, Object>> getMapList(String selectType, List<MaterialsReportVo> result, List<LocalDate> rangeDays) {
        List<Map<String, Object>> mapList = new ArrayList<>();

        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (MaterialsReportVo reportVo : result) {
            Map<String, Object> itemMap = new LinkedHashMap<>();
            List<MaterialsReportItemVo> itemList = reportVo.getItemList();

            itemMap.put("productName", reportVo.getProductName());
            itemMap.put("specification", reportVo.getSpecification());
            itemMap.put("color", reportVo.getColor());
            if (selectType.equals("原材料")) {
                for (LocalDate rangeDay : rangeDays) {
                    String bizDate = rangeDay.format(dayFormatter);
                    for (MaterialsReportItemVo itemVo : itemList) {
                        if (itemVo.getBizDate().equals(bizDate)) {
                            itemMap.put(bizDate + "-piQty", itemVo.getPiQty());
                            itemMap.put(bizDate + "-qty", itemVo.getQty());
                        }
                    }
                }
            } else if (selectType.equals("辅料")) {
                for (LocalDate rangeDay : rangeDays) {
                    String bizDate = rangeDay.format(dayFormatter);
                    for (MaterialsReportItemVo itemVo : itemList) {
                        if (itemVo.getBizDate().equals(bizDate)) {
                            itemMap.put(bizDate + "-qty", itemVo.getQty());
                        }
                    }
                }
            }
            itemMap.put("totalPiQty", reportVo.getTotalPiQty());
            itemMap.put("totalQty", reportVo.getTotalQty());
            mapList.add(itemMap);
        }
        Map<String, Object> totalMap = new LinkedHashMap<>();


        for (Map<String, Object> stringObjectMap : mapList) {
            for (String s : stringObjectMap.keySet()) {
                totalMap.put(s, strToDecimal(StringUtils.isNull(totalMap.get(s)) ?"0":totalMap.get(s).toString())
                        .add(strToDecimal(StringUtils.isNull(stringObjectMap.get(s)) ?"0":stringObjectMap.get(s).toString()))
                );
            }
        }
        totalMap.put("productName", "合计");
        totalMap.put("specification", "合计");
        totalMap.put("color", "合计");
        mapList.add(totalMap);
        return mapList;
    }

    public static BigDecimal strToDecimal(String numStr) {
        if (StringUtils.isEmpty(numStr)) {
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(numStr);
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }

    }




    private List<Map<String, String>> getHeadMapList(String selectType, List<MaterialsReportVo> result, List<LocalDate> rangeDays) {
       List<Map<String, String> >  headMapList = new ArrayList<>();
        Map<String, String> headMap = new LinkedHashMap<>();
        //先写死 可以改为反射获取
        headMap.put("productName", "布料名称");
        headMap.put("specification", "规格");
        headMap.put("color", "颜色");

        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (selectType.equals("原材料")) {
            for (LocalDate rangeDay : rangeDays) {
                String bizDate = rangeDay.format(dayFormatter);
                headMap.put(bizDate + "-piQty", rangeDay.getMonthValue()+"月"+rangeDay.getDayOfMonth()+"日");
                headMap.put(bizDate + "-qty", rangeDay.getMonthValue()+"月"+rangeDay.getDayOfMonth()+"日");
            }

            headMap.put("totalPiQty", "总计");
            headMap.put("totalQty", "总计");

        } else if (selectType.equals("辅料")) {
            for (LocalDate rangeDay : rangeDays) {
                String bizDate = rangeDay.format(dayFormatter);
                headMap.put(bizDate + "-qty", rangeDay.getMonthValue()+"月"+rangeDay.getDayOfMonth()+"日");
            }
            headMap.put("totalQty", "总计");
        }
        headMapList.add(headMap);

        if(selectType.equals("原材料")){
            Map<String, String> headMap2 = new LinkedHashMap<>();
            //先写死 可以改为反射获取
            headMap2.put("productName", "布料名称");
            headMap2.put("specification", "规格");
            headMap2.put("color", "颜色");
            for (LocalDate rangeDay : rangeDays) {
                String bizDate = rangeDay.format(dayFormatter);
                headMap2.put(bizDate + "-piQty", "匹数");
                headMap2.put(bizDate + "-qty", "重量");
            }
            headMap2.put("totalPiQty", "匹数");
            headMap2.put("totalQty", "重量");
            headMapList.add(headMap2);
        }


        return headMapList;
    }


    /**
     * 获取间隔的日期列表
     *
     * @param dateBegin 开始日期
     * @param dateEnd   截止日期
     * @return
     */
    private List<LocalDate> getRangeDays(LocalDate dateBegin, LocalDate dateEnd) {
        List<LocalDate> dates = new ArrayList<>();
        //间隔的天数
        long betweenDays = ChronoUnit.DAYS.between(dateBegin, dateEnd);
        if (betweenDays < 1) {
            //开始日期<=截止日期
            return dates;
        }
        //创建一个从开始日期、每次加一天的无限流，限制到截止日期为止
        Stream.iterate(dateBegin, c -> c.plusDays(1))
                .limit(betweenDays + 1)
                .forEach(dates::add);
        return dates;
    }
}
