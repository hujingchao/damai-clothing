package com.fenglei.model.report.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class MaterialsReportVo {

    @ApiModelProperty("物料详情id")
    private String materialDetailId;

    @ApiModelProperty("布料")
    private String productId;
    @ApiModelProperty("布料名称")
    private String productName;

    @ApiModelProperty("规格尺码id")
    private String specificationId;
    @ApiModelProperty("规格尺码")
    private String specification;

    @ApiModelProperty("颜色id")
    private String colorId;
    @ApiModelProperty("颜色")
    private String color;

    @ApiModelProperty("总数量")
    private BigDecimal totalQty;

    @ApiModelProperty("总匹数")
    private BigDecimal totalPiQty;


    private List<MaterialsReportItemVo> itemList;


}
