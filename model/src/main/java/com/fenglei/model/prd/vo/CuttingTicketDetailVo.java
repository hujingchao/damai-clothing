package com.fenglei.model.prd.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fenglei.model.prd.entity.PrdCuttingTicketItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 裁床单票据信息详情Vo
 */
@Data
public class CuttingTicketDetailVo {

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


    @ApiModelProperty("颜色id")
    private String colorId;
    @ApiModelProperty("颜色")
    private String color;

    @ApiModelProperty("规格尺码id")
    private String specificationId;
    @ApiModelProperty("规格尺码")
    private String specification;

    @ApiModelProperty("总数量")
    private BigDecimal sumQty;
    @ApiModelProperty("已上数数量")
    private BigDecimal reportedQty;

    @ApiModelProperty("票数")
    private Integer count;

    @ApiModelProperty("入库数量")
    private BigDecimal inStockQty;

    @ApiModelProperty("工序总数")
    private int progressCount;
    @ApiModelProperty("已完成工序数")
    private int finishProgressCount;
    @ApiModelProperty("上数进度（已完成工序百分比）")
    private BigDecimal progress;

    @ApiModelProperty("裁床单工票号信息")
    List<PrdCuttingTicketItem> cuttingTicketItems;
}
