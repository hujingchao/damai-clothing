package com.fenglei.model.system.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 首页生产订单量
 */
@Data
public class DashboardMoInfo {

    @ApiModelProperty("时间")
    private List<String> dateList;
    @ApiModelProperty("订单数")
    private List<Integer> countList;

    private String date;

    private int count;
}
