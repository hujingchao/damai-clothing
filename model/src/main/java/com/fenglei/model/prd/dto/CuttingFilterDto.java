package com.fenglei.model.prd.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 裁床单过滤条件
 */
@Data
public class CuttingFilterDto {

    @ApiModelProperty("款号，裁床单号")
    private String no;

    @ApiModelProperty("大货款号，编码")
    private String productNum;

    @ApiModelProperty("物料ids")
    private List<String> productArr;

    @ApiModelProperty("物料id")
    private String productId;

    @ApiModelProperty("颜色")
    private String color;

    @ApiModelProperty("尺寸")
    private String specification;

    @ApiModelProperty("机床号，编号")
    private String equipmentNum;

    @ApiModelProperty("设备id")
    private List<String> equipmentArr;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("裁床id")
    private String cuttingId;

    @ApiModelProperty("裁床开始日期")
    private String cuttingStartDate;

    @ApiModelProperty("裁床结束日期")
    private String cuttingEndDate;

    @ApiModelProperty("交货开始日期")
    private String deliveryStartDate;

    @ApiModelProperty("交货结束日期")
    private String deliveryEndDate;

    @ApiModelProperty("只查询待上数的数据")
    private Boolean onlyShowWaitReport;

    @ApiModelProperty("源单id 查询接口")
    private String srcId;

    @ApiModelProperty("查询关键字")
    private String keyWord;

    @ApiModelProperty("生产数量")
    private BigDecimal productQty;
    @ApiModelProperty("已汇报数量")
    private BigDecimal reportedQty;
    @ApiModelProperty("已完成数量")
    private BigDecimal finishedQty;

    @ApiModelProperty("总汇报数量")
    private BigDecimal totalCount;
    @ApiModelProperty("总汇报金额")
    private BigDecimal totalAmount;

    @ApiModelProperty("工票号")
    private String ticketNo;
    @ApiModelProperty("日期排序")
    private Integer dateSort;

    @ApiModelProperty("是否已完工（所有末道工序都已汇报）")
    private Boolean reportedStatus;

}
