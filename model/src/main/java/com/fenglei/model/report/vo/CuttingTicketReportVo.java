package com.fenglei.model.report.vo;

import com.fenglei.model.base.pojo.ExcelColumn;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
@Data
public class CuttingTicketReportVo {

    @ApiModelProperty("裁床日期")
    @ExcelColumn(value = "裁床日期", col = 1)
    private String cuttingDate;

    @ApiModelProperty("产品")
    private String productId;

    @ApiModelProperty("产品名称")
    @ExcelColumn(value = "产品名称", col = 2)
    private String productName;

    @ApiModelProperty("颜色id")
    private String colorId;
    @ApiModelProperty("颜色")
    @ExcelColumn(value = "颜色", col = 3)
    private String color;

    @ApiModelProperty("规格尺码id")
    private String specificationId;
    @ApiModelProperty("规格尺码")
    @ExcelColumn(value = "规格尺码", col = 4)
    private String specification;

    @ApiModelProperty("裁床成品")
    @ExcelColumn(value = "裁床成品", col = 5)
    private BigDecimal sumQty;

    @ApiModelProperty("入库数量")
    @ExcelColumn(value = "入库数量", col = 5)
    private BigDecimal finishQty;

    @ApiModelProperty("布料匹数")
    @ExcelColumn(value = "布料匹数", col = 6)
    private BigDecimal realPiQty;

    @ApiModelProperty("布料重量")
    @ExcelColumn(value = "布料重量", col = 7)
    private BigDecimal realQty;

    @ApiModelProperty("裁床用量")
    @ExcelColumn(value = "裁床用量", col = 8)
    private BigDecimal usedQty;

    @ApiModelProperty("理论用量")
    private BigDecimal theoryQty;

    @ApiModelProperty("标准用量")
    @ExcelColumn(value = "标准用量", col = 9)
    private BigDecimal standardQty;

    @ApiModelProperty("误差")
    @ExcelColumn(value = "误差", col = 10)
    private BigDecimal error;

    @ApiModelProperty("误差率")
    @ExcelColumn(value = "误差率", col = 11)
    private BigDecimal errorRate;


}
