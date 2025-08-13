package com.fenglei.model.fin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 计件工资过滤条件
 */
@Data
public class PieceRateFilter {

    @ApiModelProperty("开始日期")
    private String beginDate;
    @ApiModelProperty("结束日期")
    private String endDate;
    @ApiModelProperty("日期范围")
    private List<String> filterDateArr;

    @ApiModelProperty("部门id")
    private List<String> deptIds;

    @ApiModelProperty("员工id")
    private List<String> staffIds;

    private String color;

    private String specification;

    private String productName;

    private String number;

    private String procedureName;

    private String staffName;

    private String staffId;

    @ApiModelProperty("本月开始日期")
    private String currentMonthBeginDate;
    @ApiModelProperty("本月结束日期")
    private String currentMonthEndDate;

    @ApiModelProperty("上月开始日期")
    private String lastMonthBeginDate;
    @ApiModelProperty("上月结束日期")
    private String lastMonthEndDate;
}
