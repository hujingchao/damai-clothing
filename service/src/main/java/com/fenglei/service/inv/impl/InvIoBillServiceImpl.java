package com.fenglei.service.inv.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fenglei.common.util.ExcelUtils;
import com.fenglei.common.util.StringUtils;
import com.fenglei.model.fin.vo.PieceRateVo;
import com.fenglei.model.inv.entity.InvIoBill;
import com.fenglei.mapper.inv.InvIoBillMapper;
import com.fenglei.service.inv.IInvIoBillService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 物料收发流水账 服务实现类
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-22
 */
@Service
public class InvIoBillServiceImpl extends ServiceImpl<InvIoBillMapper, InvIoBill> implements IInvIoBillService {

    /**
     * 新增
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public InvIoBill add(InvIoBill invIoBill) {
        this.save(invIoBill);
        return invIoBill;
    }

    /**
     * 删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean myRemoveById(String id) {
        return this.removeById(id);
    }

    /**
     * 批量删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean myRemoveByIds(List<String> ids) {
        return this.removeByIds(ids);
    }

    /**
     * 更新
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean myUpdate(InvIoBill invIoBill) {
        return this.updateById(invIoBill);
    }

    /**
     * 分页查询
     */
    @Override
    public IPage<InvIoBill> myPage(Page<InvIoBill> page, InvIoBill invIoBill) {
        List<String> filterDateArr = invIoBill.getFilterDateArr();
        if (StringUtils.isNotEmpty(filterDateArr)) {
            invIoBill.setBeginDate(filterDateArr.get(0));
            invIoBill.setEndDate(filterDateArr.get(1));
        }
        return this.baseMapper.myPage(page, invIoBill);
    }

    /**
     * 详情
     */
    @Override
    public InvIoBill detail(String id) {
        return this.getById(id);
    }

    /**
     * 导出
     */
    @Override
    public void exportInvIoBill(HttpServletResponse response, InvIoBill invIoBill) {
        List<String> filterDateArr = invIoBill.getFilterDateArr();
        if (StringUtils.isNotEmpty(filterDateArr)) {
            invIoBill.setBeginDate(filterDateArr.get(0));
            invIoBill.setEndDate(filterDateArr.get(1));
        }

        List<InvIoBill> invIoBills = this.baseMapper.myPage(invIoBill);
        // 创建一个excel
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 新建sheet页
        XSSFSheet sheet = workbook.createSheet("sheet1");
        //设置默认宽度好像必须先设置默认高度，不然不生效。。。。。。
        sheet.setDefaultRowHeight((short) (1 * 256));
        sheet.setDefaultColumnWidth(20);


        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont fontStyle = workbook.createFont();
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

        XSSFCellStyle cellStyleContent = workbook.createCellStyle();
        cellStyleContent.setAlignment(HorizontalAlignment.CENTER);
        cellStyleContent.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置边框
        cellStyleContent.setBorderTop(BorderStyle.THIN);
        cellStyleContent.setBorderBottom(BorderStyle.THIN);
        cellStyleContent.setBorderLeft(BorderStyle.THIN);
        cellStyleContent.setBorderRight(BorderStyle.THIN);
        XSSFFont fontStyleContent = workbook.createFont();
        fontStyle.setFontHeightInPoints((short) 12);
        cellStyleContent.setFont(fontStyleContent);


        //第一行   头
        XSSFRow row2 = sheet.createRow(sheet.getLastRowNum() + 1);
        row2.setHeightInPoints(20);
        List<String> title = new ArrayList<>();
        title.add("序号");
        title.add("商品分类");
        title.add("商品名称");
        title.add("商品编码");
        title.add("商家编码");
        title.add("日期");
        title.add("包号");
        title.add("颜色");
        title.add("规格型号");
        title.add("单据类型");
        title.add("出入");
        title.add("数量");
        title.add("匹数");
        title.add("仓库");
        title.add("仓位");
        for (int i = 0; i < title.size(); i++) {
            XSSFCell cel = row2.createCell(i);
            cel.setCellStyle(cellStyle);
            cel.setCellValue(title.get(i));
        }
        // 数据
        BigDecimal qtySum = BigDecimal.ZERO;
        BigDecimal piQtySum = BigDecimal.ZERO;
        int size = invIoBills.size();
        for (int j = 0; j < size; j++) {
            InvIoBill record = invIoBills.get(j);
            BigDecimal qty = StringUtils.isNull(record.getQty()) ? BigDecimal.ZERO : record.getQty();
            BigDecimal piQty = StringUtils.isNull(record.getPiQty()) ? BigDecimal.ZERO : record.getPiQty();
            String io = record.getIo();
            if (io.equals("出库")) {
                qtySum = qtySum.subtract(qty);
                piQtySum = piQtySum.subtract(piQty);
            } else {
                qtySum = qtySum.add(qty);
                piQtySum = piQtySum.add(piQty);
            }

            XSSFRow rowData = sheet.createRow(sheet.getLastRowNum() + 1);
            rowData.setHeightInPoints(15);
            rowData.createCell(0).setCellValue(j + 1);
            rowData.createCell(1).setCellValue(record.getMaterialGroupName());
            rowData.createCell(2).setCellValue(record.getProductName());
            rowData.createCell(3).setCellValue(record.getProductNum());
            rowData.createCell(4).setCellValue(record.getSjNumber());
            rowData.createCell(5).setCellValue(record.getBizDate());
            rowData.createCell(6).setCellValue(record.getLot());
            rowData.createCell(7).setCellValue(record.getColor());
            rowData.createCell(8).setCellValue(record.getSpecification());
            rowData.createCell(9).setCellValue(record.getSrcType());
            rowData.createCell(10).setCellValue(io);
            rowData.createCell(11).setCellValue(String.valueOf(record.getQty()));
            rowData.createCell(12).setCellValue(String.valueOf(record.getPiQty()));
            rowData.createCell(13).setCellValue(record.getRepositoryName());
            rowData.createCell(14).setCellValue(record.getPositionName());
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
        XSSFRow rowData = sheet.createRow(sheet.getLastRowNum() + 1);
        rowData.setHeightInPoints(15);
        rowData.createCell(0).setCellValue("合计");
        rowData.createCell(1).setCellValue("");
        rowData.createCell(2).setCellValue("");
        rowData.createCell(3).setCellValue("");
        rowData.createCell(4).setCellValue("");
        rowData.createCell(5).setCellValue("");
        rowData.createCell(6).setCellValue("");
        rowData.createCell(7).setCellValue("");
        rowData.createCell(8).setCellValue("");
        rowData.createCell(9).setCellValue("");
        rowData.createCell(10).setCellValue("");
        rowData.createCell(11).setCellValue(String.valueOf(qtySum));
        rowData.createCell(12).setCellValue(String.valueOf(piQtySum));
        rowData.createCell(13).setCellValue("");
        rowData.createCell(14).setCellValue("");
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
        ExcelUtils.buildXlsxDocument("物料收发明细账", workbook, response);
    }
}
