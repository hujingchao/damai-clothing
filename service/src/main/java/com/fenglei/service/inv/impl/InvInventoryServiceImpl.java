package com.fenglei.service.inv.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.common.exception.BizException;
import com.fenglei.common.util.ExcelUtils;
import com.fenglei.common.util.StringUtils;
import com.fenglei.mapper.inv.InvInventoryMapper;
import com.fenglei.model.basedata.BdMaterial;
import com.fenglei.model.basedata.dto.MaterialDTO;
import com.fenglei.model.fin.vo.PieceRateVo;
import com.fenglei.model.inv.entity.InvInventory;
import com.fenglei.model.inv.entity.InvIoBill;
import com.fenglei.model.inv.dto.InventoryDto;
import com.fenglei.service.inv.IInvIoBillService;
import com.fenglei.service.inv.InvInventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvInventoryServiceImpl extends ServiceImpl<InvInventoryMapper, InvInventory> implements InvInventoryService {

    @Autowired
    IInvIoBillService ioBillService;

    @Override
    public IPage<InvInventory> myPage(Page page, InvInventory invInventory) {

        IPage<InvInventory> iPage = baseMapper.getPage(page, invInventory);

        return iPage;
    }

    @Override
    public List<InvInventory> myList(InvInventory invInventory) {
        List<InvInventory> list = baseMapper.getList(invInventory);

        return list;
    }

    @Override
    public InvInventory detail(String id) {
        InvInventory result = baseMapper.infoById(id);

        return result;
    }

    /**
     * 新增即时库存
     *
     * @param invInventory 新增信息
     * @param invIoBill    源单信息
     * @param ioType       1：增加流水账，2：删除流水账
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addQty(InvInventory invInventory, InvIoBill invIoBill, int ioType) {
        if (ioType == 1) {
            InvIoBill addInvIoBill = new InvIoBill();
            BeanUtils.copyProperties(invIoBill, addInvIoBill);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            addInvIoBill.setBizDate(sdf.format(new Date()));
            addInvIoBill.setIo("入库");
            addInvIoBill.setMaterialDetailId(invInventory.getMaterialDetailId());
            addInvIoBill.setQty(invInventory.getQty());
            addInvIoBill.setPiQty(invInventory.getPiQty());
            addInvIoBill.setRepositoryId(invInventory.getRepositoryId());
            addInvIoBill.setPositionId(invInventory.getPositionId());
            addInvIoBill.setLot(invInventory.getLot());
            ioBillService.save(addInvIoBill);
        } else if (ioType == 2) {
            ioBillService.remove(Wrappers.lambdaQuery(InvIoBill.class)
                    .eq(InvIoBill::getSrcType, invIoBill.getSrcType())
                    .eq(InvIoBill::getSrcItemId, invIoBill.getSrcItemId())
                    .eq(InvIoBill::getSrcId, invIoBill.getSrcId())
            );
        }

        if (invInventory.getQty() == null) {
            throw new BizException("请填写数量");
        }
        if (StringUtils.isEmpty(invInventory.getMaterialDetailId())) {
            throw new BizException("请选择物料");
        }
        if (StringUtils.isEmpty(invInventory.getRepositoryId())) {
            throw new BizException("请填写仓库");
        }

        if (invInventory.getPrice() == null) {
            invInventory.setPrice(BigDecimal.ZERO);
        }

        List<InvInventory> list = baseMapper.getList(invInventory);
        if (!list.isEmpty()) {
            InvInventory inv = list.get(0);

            BigDecimal amount = inv.getQty().multiply(inv.getPrice());
            amount = amount.add(invInventory.getQty().multiply(invInventory.getPrice()));

            BigDecimal qty = inv.getQty().add(invInventory.getQty());
            inv.setQty(qty);

            BigDecimal price = amount.divide(qty, 3, RoundingMode.HALF_UP);
            inv.setPrice(price);

            if (invInventory.getPiQty() != null) {
                BigDecimal piQty = inv.getPiQty() == null ? BigDecimal.ZERO : inv.getPiQty();
                piQty = piQty.add(invInventory.getPiQty());
                inv.setPiQty(piQty);
            }

            if (!this.updateById(inv)) {
                throw new BizException("即时库存更新失败");
            }
        } else {
            if (!this.save(invInventory)) {
                throw new BizException("即时库存保存失败");
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean batchAddQty(List<InvInventory> invInventorys, List<InvIoBill> invIoBills, int ioType) {
        if (invInventorys.isEmpty()) {
            throw new BizException("数据不可及为空");
        }
        int index = 0;
        for (InvInventory invInventory : invInventorys) {
            InvIoBill invIoBill = null;
            if (ioType == 1 || ioType == 2) {
                invIoBill = invIoBills.get(index++);
            }
            if (!this.addQty(invInventory, invIoBill, ioType)) {
                throw new BizException("即时库存更新失败");
            }
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean subQty(InvInventory invInventory, InvIoBill invIoBill, int ioType) {

        if (ioType == 1) {
            InvIoBill addInvIoBill = new InvIoBill();
            BeanUtils.copyProperties(invIoBill, addInvIoBill);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            addInvIoBill.setBizDate(sdf.format(new Date()));
            addInvIoBill.setIo("出库");
            addInvIoBill.setMaterialDetailId(invInventory.getMaterialDetailId());
            addInvIoBill.setQty(invInventory.getQty());
            addInvIoBill.setPiQty(invInventory.getPiQty());
            addInvIoBill.setRepositoryId(invInventory.getRepositoryId());
            addInvIoBill.setPositionId(invInventory.getPositionId());
            addInvIoBill.setLot(invInventory.getLot());
            ioBillService.save(addInvIoBill);
        } else if (ioType == 2) {
            ioBillService.remove(Wrappers.lambdaQuery(InvIoBill.class)
                    .eq(InvIoBill::getSrcType, invIoBill.getSrcType())
                    .eq(InvIoBill::getSrcItemId, invIoBill.getSrcItemId())
                    .eq(InvIoBill::getSrcId, invIoBill.getSrcId())
            );
        }

        if (invInventory.getQty() == null) {
            throw new BizException("请填写数量");
        }
        if (StringUtils.isEmpty(invInventory.getMaterialDetailId())) {
            throw new BizException("请选择物料");
        }

        List<InvInventory> list = baseMapper.getList(invInventory);
        if (list != null && !list.isEmpty()) {
            List<InvInventory> udtInvs = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                InvInventory inv = list.get(i);

                if (inv.getQty().compareTo(invInventory.getQty()) >= 0) {
                    BigDecimal amount = inv.getQty().multiply(inv.getPrice());
                    if (invInventory.getPrice() != null) {
                        amount = amount.subtract(invInventory.getQty().multiply(invInventory.getPrice()));
                    }

                    inv.setQty(inv.getQty().subtract(invInventory.getQty()));
                    invInventory.setQty(BigDecimal.ZERO);

                    BigDecimal price = BigDecimal.ZERO;
                    if (inv.getQty().compareTo(BigDecimal.ZERO) == 1) {
                        price = amount.divide(inv.getQty(), 3, BigDecimal.ROUND_HALF_UP);
                    }
                    inv.setPrice(price);

                    if (invInventory.getPiQty() != null) {
                        if (inv.getPiQty() != null) {
                            if (inv.getPiQty().compareTo(invInventory.getPiQty()) >= 0) {
                                inv.setPiQty(inv.getPiQty().subtract(invInventory.getPiQty()));

                                invInventory.setPiQty(BigDecimal.ZERO);
                            } else {
                                invInventory.setPiQty(invInventory.getPiQty().subtract(inv.getPiQty()));

                                inv.setPiQty(BigDecimal.ZERO);
                            }
                        }
                    }

                    udtInvs.add(inv);

                    break;
                } else {
                    invInventory.setQty(invInventory.getQty().subtract(inv.getQty()));

                    inv.setQty(BigDecimal.ZERO);
                    inv.setPrice(BigDecimal.ZERO);

                    if (invInventory.getPiQty() != null) {
                        if (inv.getPiQty() != null) {
                            if (inv.getPiQty().compareTo(invInventory.getPiQty()) >= 0) {
                                inv.setPiQty(inv.getPiQty().subtract(invInventory.getPiQty()));

                                invInventory.setPiQty(BigDecimal.ZERO);
                            }
                        }
                    }

                    udtInvs.add(inv);
                }
            }

            if (invInventory.getPiQty() != null && invInventory.getPiQty().compareTo(BigDecimal.ZERO) > 0) {
                throw new BizException("即时库存更新失败，匹数异常");
            }

            if (invInventory.getQty() != null && invInventory.getQty().compareTo(BigDecimal.ZERO) > 0) {
                throw new BizException("即时库存更新失败，数量异常");
            }

            if (!this.updateBatchById(udtInvs)) {
                throw new BizException("即时库存更新失败");
            }
        } else {
            throw new BizException("未检测到相应的即时库存数据");
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean batchSubQty(List<InvInventory> invInventorys, List<InvIoBill> invIoBills, int ioType) {
        if (!invInventorys.isEmpty()) {
            int index = 0;
            for (InvInventory invInventory : invInventorys) {
                InvIoBill invIoBill = null;
                if (ioType == 1 || ioType == 2) {
                    invIoBill = invIoBills.get(index++);
                }
                if (!this.subQty(invInventory, invIoBill, ioType)) {
                    throw new BizException("即时库存更新失败");
                }
            }
        } else {
            throw new BizException("数据不可及为空");
        }

        return true;
    }

    @Override
    public IPage getPageByLot(Page page, InvInventory invInventory) {
        IPage<InvInventory> iPage = baseMapper.getPageByLot(page, invInventory);
        return iPage;
    }

    @Override
    public IPage<InventoryDto> pageForPackage(Page<InventoryDto> page, InventoryDto inventoryDto) {
        return this.baseMapper.pageForPackage(page, inventoryDto);
    }

    @Override
    public List<MaterialDTO> getMaterialsByLots(List<String> lots) {
        return baseMapper.getMaterialsByLots(lots);
    }

    @Override
    public void exportInventory(HttpServletResponse response, InvInventory invInventory) {
        List<InvInventory> list = this.baseMapper.getPage(invInventory);
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
        title.add("物料编码");
        title.add("物料名称");
        title.add("物料类别");
        title.add("颜色/色号");
        title.add("规格");
        title.add("单位");
        title.add("库存");
        title.add("仓库");
        title.add("货位");
        title.add("匹数");
        title.add("包号");
        title.add("单价(元)");
        for (int i = 0; i < title.size(); i++) {
            HSSFCell cel = row2.createCell(i);
            cel.setCellStyle(cellStyle);
            cel.setCellValue(title.get(i));
        }
        // 数据
        for (int j = 0, recordsSize = list.size(); j < recordsSize; j++) {
            InvInventory record = list.get(j);
            HSSFRow rowData = sheet.createRow(sheet.getLastRowNum() + 1);
            rowData.setHeightInPoints(15);
            rowData.createCell(0).setCellValue(j + 1);
            BdMaterial material = record.getMaterial();
            rowData.createCell(1).setCellValue(material.getNumber());
            rowData.createCell(2).setCellValue(material.getName());
            Integer materialGroup = material.getMaterialGroup();
            if (materialGroup.equals(0)) {
                rowData.createCell(3).setCellValue("成品");
            } else if (materialGroup.equals(1)) {
                rowData.createCell(3).setCellValue("辅料");
            } else if (materialGroup.equals(2)) {
                rowData.createCell(3).setCellValue("原材料");
            }
            rowData.createCell(4).setCellValue(record.getColor());
            rowData.createCell(5).setCellValue(record.getSpecification());
            rowData.createCell(6).setCellValue(material.getUnitName());
            rowData.createCell(7).setCellValue(String.valueOf(record.getQty()));
            rowData.createCell(8).setCellValue(record.getRepositoryName());
            rowData.createCell(9).setCellValue(record.getPositionName());
            rowData.createCell(10).setCellValue(String.valueOf(record.getPiQty()));
            rowData.createCell(11).setCellValue(record.getLot());
            rowData.createCell(12).setCellValue(String.valueOf(record.getPrice()));
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
        }
        ExcelUtils.buildXlsxDocument("即时库存", workbook, response);
    }
}
