package com.fenglei.model.prd.dto;

import com.fenglei.model.basedata.BdMaterial;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PrdMoDTO {
    @ApiModelProperty("编号")
    private String no;

    @ApiModelProperty("单据类型")
    private String billType;

    @ApiModelProperty("交货日期")
    private String deliveryDate;

    @ApiModelProperty("大货款号，名称")
    private String productName;

    @ApiModelProperty("商家编码")
    private String productNumber;

    @ApiModelProperty(value = "跟单员")
    private String follower;

    @ApiModelProperty("预开工日")
    private String preBeginDate;

    @ApiModelProperty("预完工日")
    private String preEndDate;

    @ApiModelProperty(value = "负责人")
    private String leader;

    @ApiModelProperty("标签")
    private String tags;
    @ApiModelProperty("颜色")
    private String color;
    @ApiModelProperty("规格")
    private String specification;
    @ApiModelProperty("数量")
    private String qty;
    @ApiModelProperty("工序名称")
    private String procedureName;
    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("标签")
    private String auxId;
    @ApiModelProperty("标签")
    private BigDecimal auxQty;
    @ApiModelProperty("辅料")
    private BdMaterial auxMaterial;
    @ApiModelProperty("标签")
    private String detailId;
    @ApiModelProperty("标签")
    private BigDecimal detailQty;
    @ApiModelProperty("原材料")
    private BdMaterial auxDetail;



}
