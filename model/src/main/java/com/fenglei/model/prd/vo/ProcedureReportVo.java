package com.fenglei.model.prd.vo;

import com.fenglei.model.prd.entity.PrdCuttingTicket;
import com.fenglei.model.prd.entity.PrdCuttingTicketItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * app端工序上数
 */
@Data
public class ProcedureReportVo {


    @ApiModelProperty("商品id")
    private String productId;
    @ApiModelProperty("裁床单工序id")
    private String cuttingRouteId;
    @ApiModelProperty("工序行号")
    private int procedureSeq;
    @ApiModelProperty("工序id")
    private String procedureId;
    @ApiModelProperty("工序号")
    private int procedureNo;
    @ApiModelProperty("工序编码")
    private String procedureNum;
    @ApiModelProperty("工序名称")
    private String procedureName;
    @ApiModelProperty("工序总数")
    private BigDecimal procedureQty;
    @ApiModelProperty("工序已完成数")
    private BigDecimal procedureReportedQty;
    @ApiModelProperty("工序剩余未完成数")
    private BigDecimal procedureUnReportQty;

    @ApiModelProperty("工票信息")
    private List<PrdCuttingTicket> cuttingTickets;

    @ApiModelProperty("工票扎号信息")
    private List<PrdCuttingTicketItem> cuttingTicketItems;
}
