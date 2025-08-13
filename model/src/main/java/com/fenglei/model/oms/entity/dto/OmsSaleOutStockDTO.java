package com.fenglei.model.oms.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OmsSaleOutStockDTO {
    @ApiModelProperty("出库日期")
    private String outStockDate;

    @ApiModelProperty(value = "商家编码")
    private String sjNumber;

    @ApiModelProperty(value = "货品编码")
    private String materialNumber;

    @ApiModelProperty(value = "货品名称")
    private String materialName;

    @ApiModelProperty(value = "颜色")
    private String color;

    @ApiModelProperty(value = "规格")
    private String specification;

    @ApiModelProperty(value = "数量")
    private String qty;

    @ApiModelProperty(value = "包数")
    private String lotQty;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "开始")
    private String outStockDateStart;

    @ApiModelProperty(value = "结束")
    private String outStockDateEnd;
}
