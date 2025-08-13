package com.fenglei.model.fin.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 计件工资
 */
@Data
public class PieceRateVo {

    @ApiModelProperty("员工id")
    private String staffId;
    @ApiModelProperty("员工编码")
    private String staffNum;
    @ApiModelProperty("员工名称")
    private String staffName;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("工序id")
    private String procedureId;
    @ApiModelProperty("工序号")
    private int procedureNo;
    @ApiModelProperty("工序编码")
    private String procedureNum;
    @ApiModelProperty("工序名称")
    private String procedureName;

    @ApiModelProperty("汇报时间")
    private String reportTime;

    @ApiModelProperty("是否收货入库")
    private Boolean isInStock;
    @ApiModelProperty("工价")
    private BigDecimal price;
    @ApiModelProperty("上数汇报数量")
    private BigDecimal realReportedQty;
    @ApiModelProperty("数量")
    private BigDecimal qty;
    @ApiModelProperty("预估工资")
    private BigDecimal predictAmount;
    @ApiModelProperty("工资")
    private BigDecimal amount;

    @ApiModelProperty("工票号")
    private String ticketNo;

    @ApiModelProperty("颜色")
    private String color;
    @ApiModelProperty("规格")
    private String specification;
    @ApiModelProperty("商品名称")
    private String productName;
    @ApiModelProperty("skuNum")
    private String number;

    @ApiModelProperty("上月工资")
    private BigDecimal lastMonthAmount;
    @ApiModelProperty("本月工资")
    private BigDecimal currentMonthAmount;
    @ApiModelProperty("上月工资")
    private BigDecimal todayAmount;
}
