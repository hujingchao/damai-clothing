package com.fenglei.model.report.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MaterialsReportItemVo {


    @ApiModelProperty("物料详情id")
    private String materialDetailId;

    @ApiModelProperty("入库日期")
    private String bizDate;

    @ApiModelProperty("数量")
    private BigDecimal qty;

    @ApiModelProperty("匹数")
    private BigDecimal piQty;
}
