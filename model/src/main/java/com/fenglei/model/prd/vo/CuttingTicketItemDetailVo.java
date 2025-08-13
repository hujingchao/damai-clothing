package com.fenglei.model.prd.vo;

import com.fenglei.model.prd.entity.PrdCuttingRoute;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 扎号上数详情
 */
@Data
public class CuttingTicketItemDetailVo {
    @ApiModelProperty("裁床单id")
    private String cuttingId;
    @ApiModelProperty("裁床单编号")
    private String cuttingNum;
    @ApiModelProperty("机床id")
    private String equipmentId;
    @ApiModelProperty("机床号")
    private String equipmentNum;

    @ApiModelProperty("大货款号（成品id）")
    private String productId;
    @ApiModelProperty("skuId")
    private String skuId;
    @ApiModelProperty("大货款号，编码")
    private String productNum;
    @ApiModelProperty("大货款号，名称")
    private String productName;
    @ApiModelProperty("主图Id")
    private String mainPicId;
    @ApiModelProperty("主图")
    private String mainPic;
    @ApiModelProperty("生产数量")
    private BigDecimal productQty;

    @ApiModelProperty("交货日期")
    private String deliveryDate;
    @ApiModelProperty("裁床日期")
    private String cuttingDate;

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

    @ApiModelProperty("总数量")
    private BigDecimal qty;
    @ApiModelProperty("已上数数量")
    private BigDecimal reportedQty;

    @ApiModelProperty("入库数量")
    private BigDecimal inStockQty;

    @ApiModelProperty("包装数量")
    private BigDecimal packQty;

    @ApiModelProperty("历史保存时的数量")
    private BigDecimal saveQty;

    @ApiModelProperty("生产订单/计划单")
    private String srcNo;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("汇报详情")
    private List<PrdCuttingRoute> cuttingRoutes;
}
