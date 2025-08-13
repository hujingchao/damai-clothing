package com.fenglei.model.system.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 首页生产信息
 */
@Data
public class DashboardProductionInfo {

    @ApiModelProperty("日完工量")
    private BigDecimal todayFinishedQty;
    @ApiModelProperty("在制量")
    private BigDecimal inProductionQty;
    @ApiModelProperty("今日薪资")
    private BigDecimal todaySalary;
}
