package com.fenglei.model.prd.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 工序上数Dto
 */
@Data
public class DoProcedureReportDto {

    @ApiModelProperty("上数人id")
    private String reporterId;

    @ApiModelProperty("裁床单工序id")
    private String cuttingRouteId;
    @ApiModelProperty("工序id")
    private String procedureId;
    @ApiModelProperty("工序行号")
    private int procedureSeq;

    @ApiModelProperty("商品id")
    private String productId;
    @ApiModelProperty("裁床单id")
    private String cuttingId;
    @ApiModelProperty("工票信息id")
    private String cuttingTicketId;
    @ApiModelProperty("工票信息详情id")
    private String cuttingTicketItemId;

    @ApiModelProperty("本次上数数量（汇报数量）")
    private BigDecimal reportingQty;
}
