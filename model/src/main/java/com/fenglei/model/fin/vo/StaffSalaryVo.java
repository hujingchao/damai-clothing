package com.fenglei.model.fin.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 员工工资
 */
@Data
public class StaffSalaryVo {

    @ApiModelProperty("员工id")
    private String staffId;
    @ApiModelProperty("员工编码")
    private String staffNum;
    @ApiModelProperty("员工名称")
    private String staffName;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("基本工资")
    private BigDecimal basicSalary;

    @ApiModelProperty("总工资")
    private BigDecimal allSalary;

    @ApiModelProperty("计件工资")
    private Map<String, BigDecimal> pieceRates;
}
