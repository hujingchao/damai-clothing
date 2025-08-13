package com.fenglei.model.inv.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class InventoryDto {

    @ApiModelProperty("即时库存id")
    private String id;

    @ApiModelProperty(value = "物料分类：0-成品；1-辅料；2-原材料；")
    private Integer materialGroup;
    private List<Integer> inMaterialGroup;

    @ApiModelProperty("产品id")
    private String productId;
    @ApiModelProperty("大货款号，编码")
    private String productNum;
    @ApiModelProperty("大货款号，名称")
    private String productName;
    @ApiModelProperty("主图")
    private String mainPicUrl;

    @ApiModelProperty(value = "skuId")
    private String skuId;
    @ApiModelProperty(value = "商家编码")
    private String skuNum;

    @ApiModelProperty("单位id")
    private String unitId;
    @ApiModelProperty(value = "单位")
    private String unitName;

    @ApiModelProperty("单价")
    private BigDecimal price;

    @ApiModelProperty(value = "颜色/色号id")
    private String colorId;
    @ApiModelProperty(value = "颜色/色号")
    private String color;

    @ApiModelProperty("规格id")
    private String specificationId;
    @ApiModelProperty("规格")
    private String specification;

    @ApiModelProperty("数量")
    private BigDecimal qty;

    @ApiModelProperty(value = "仓库id")
    private String repositoryId;
    @ApiModelProperty(value = "仓库名称")
    private String repositoryName;
    @ApiModelProperty(value = "货位id")
    private String positionId;
    @ApiModelProperty(value = "货位名称")
    private String positionName;

    @ApiModelProperty(value = "已经选择的库存id")
    private List<String> selectedIds;
}
