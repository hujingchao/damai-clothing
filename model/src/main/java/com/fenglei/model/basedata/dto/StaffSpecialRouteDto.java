package com.fenglei.model.basedata.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 员工的特殊工价信息Dto
 */
@Data
public class StaffSpecialRouteDto {
    @ApiModelProperty("工序号")
    private Integer no;

    @ApiModelProperty(value = "物料id")
    private String productId;

    @ApiModelProperty(value = "工序id")
    private String procedureId;

    @ApiModelProperty(value = "工价(元)")
    private BigDecimal commonPrice;

    @ApiModelProperty(value = "员工id")
    private String staffId;

    @ApiModelProperty(value = "工价(元)")
    private BigDecimal specialPrice;

    @ApiModelProperty(value = "备注")
    private String remark;
}