package com.fenglei.model.prd.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 本月上数记录Vo
 */
@Data
public class CurMonthReportVo {
    @ApiModelProperty("裁床单id")
    private String cuttingId;
    @ApiModelProperty("裁床单号")
    private String cuttingNo;
    @ApiModelProperty("裁床单工序id")
    private String cuttingRouteId;
    @ApiModelProperty("裁床单票据信息id")
    private String cuttingTicketId;
    @ApiModelProperty("裁床单票据信息详情id")
    private String cuttingTicketItemId;

    @ApiModelProperty("大货款号（成品id）")
    private String productId;
    @ApiModelProperty("大货款号，编码")
    private String productNum;
    @ApiModelProperty("大货款号，名称")
    private String productName;
    @ApiModelProperty("主图")
    private String mainPic;

    @ApiModelProperty("交货日期")
    private String deliveryDate;
    @ApiModelProperty("裁床日期")
    private String cuttingDate;

    @ApiModelProperty("机床号（设备id）")
    private String equipmentId;
    @ApiModelProperty("机床号，编号")
    private String equipmentNum;

    @ApiModelProperty("工价(元)")
    private BigDecimal price;

    @ApiModelProperty("颜色id")
    private String colorId;
    @ApiModelProperty("颜色")
    private String color;

    @ApiModelProperty("规格尺码id")
    private String specificationId;
    @ApiModelProperty("规格尺码")
    private String specification;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("工序id")
    private String procedureId;
    @ApiModelProperty("工序号")
    private int procedureNo;
    @ApiModelProperty("工序编码")
    private String procedureNum;
    @ApiModelProperty("工序名称")
    private String procedureName;


    @ApiModelProperty("已上数数量")
    private BigDecimal reportedQty;
    @ApiModelProperty("已上数金额")
    private BigDecimal reportedAmount;


    @ApiModelProperty("上数人id")
    private String reporterId;
    @ApiModelProperty("上数人")
    private String reporter;
    @ApiModelProperty("上数时间")
    private String reportTime;

    @ApiModelProperty("创建人id")
    private String creatorId;
    @ApiModelProperty("创建人")
    private String creator;
    @ApiModelProperty("工票号")
    private String ticketNo;
    @ApiModelProperty("订单号")
    private String orderNo;
    @ApiModelProperty("数量")
    private BigDecimal qty;
    @ApiModelProperty("姓名")
    private String nickname;
    @ApiModelProperty("工号")
    private String jobNumber;

    @ApiModelProperty("入库数量")
    private BigDecimal inStockQty;
    @ApiModelProperty("入库时间")
    private String inStockTime;
    @ApiModelProperty("入库人")
    private String inStockUserName;
    @ApiModelProperty("入库整单备注")
    private String inStockRemark;
    @ApiModelProperty("入库分录备注")
    private String inStockItemRemark;

    @ApiModelProperty("原因")
    private String reason;

    private List<ProcedureVo> procedureVoList;
}
