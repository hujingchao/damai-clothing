package com.fenglei.model.prd.vo;

import com.fenglei.model.prd.entity.PrdCuttingTicketItemReport;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 工序详情
 */
@Data
public class CuttingRouteDetailVo {

    @ApiModelProperty("裁床单id")
    private String cuttingId;
    @ApiModelProperty("裁床单编号")
    private String cuttingNum;
    @ApiModelProperty("机床id")
    private String equipmentId;
    @ApiModelProperty("机床号")
    private String equipmentNum;

    @ApiModelProperty("裁床单工序id")
    private String cuttingRouteId;
    @ApiModelProperty(value = "工序id")
    private String procedureId;
    @ApiModelProperty("工序号")
    private int procedureNo;
    @ApiModelProperty("工序名称")
    private String procedureName;
    @ApiModelProperty("工序编码")
    private String procedureNum;

    @ApiModelProperty("总数量")
    private BigDecimal qty;
    @ApiModelProperty("已上数数量")
    private BigDecimal reportedQty;

    @ApiModelProperty("裁床单工票信息id")
    private String cuttingTicketId;
    @ApiModelProperty("裁床单工票信息明细id")
    private String cuttingTicketItemId;
    @ApiModelProperty("扎号")
    private String ticketNo;

    @ApiModelProperty("颜色id")
    private String colorId;
    @ApiModelProperty("颜色")
    private String color;

    @ApiModelProperty("规格尺码id")
    private String specificationId;
    @ApiModelProperty("规格尺码")
    private String specification;

    @ApiModelProperty("汇报详情")
    private List<PrdCuttingTicketItemReport> cuttingTicketItemReports;
}
