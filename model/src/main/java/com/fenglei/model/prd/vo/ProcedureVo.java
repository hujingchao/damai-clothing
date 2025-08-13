package com.fenglei.model.prd.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProcedureVo {
    @ApiModelProperty("工序id")
    private String procedureId;
    @ApiModelProperty("工序名称")
    private String procedureName;
    @ApiModelProperty("工票号")
    private String ticketNo;
    @ApiModelProperty("上数日期")
    private String reportTime;
    @ApiModelProperty("工号")
    private String jobNumber;
    @ApiModelProperty("姓名")
    private String nickname;
    @ApiModelProperty("已上数数量")
    private BigDecimal reportedQty;
    @ApiModelProperty("次品")
    private BigDecimal defectQty = BigDecimal.ZERO;
    @ApiModelProperty("原因")
    private String reason;
}
