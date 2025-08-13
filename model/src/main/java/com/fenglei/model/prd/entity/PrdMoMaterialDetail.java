package com.fenglei.model.prd.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.UBillItemBaseEntity;
import com.fenglei.model.basedata.BdMaterial;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ljw
 */
@Data
@ApiModel(value = "PrdMoMaterialDetail-生产订单 物料详情", description = "生产订单 物料详情类")
@TableName(value = "prd_mo_material_detail")
public class PrdMoMaterialDetail extends UBillItemBaseEntity {

    @ApiModelProperty(value = "物料详情id")
    private String materialDetailId;
    @TableField(exist = false)
    @ApiModelProperty(value = "物料详情ids")
    private List<String> materialDetailIds;

    @ApiModelProperty(value = "用量（分子 - KG）")
    private BigDecimal numerator;
    @ApiModelProperty(value = "用量（分母 - KG）")
    private BigDecimal denominator;
    @ApiModelProperty(value = "理论用量（KG）")
    private BigDecimal theoryQty;
    @TableField(exist = false)
    @ApiModelProperty(value = "剩余理论用量（KG）")
    private BigDecimal surplusTheoryQty;

    @TableField(exist = false)
    @ApiModelProperty(value = "其他订单用量（KG）")
    private BigDecimal otherBillQty;

    @TableField(exist = false)
    @ApiModelProperty(value = "颜色/色号id")
    private String colorId;
    @TableField(exist = false)
    @ApiModelProperty(value = "颜色/色号")
    private String color;

    @TableField(exist = false)
    @ApiModelProperty(value = "规格id")
    private String specificationId;
    @TableField(exist = false)
    @ApiModelProperty(value = "规格")
    private String specification;

    @TableField(exist = false)
    @ApiModelProperty(value = "原材料")
    private BdMaterial rawMaterial;

    @ApiModelProperty("默认供应商id")
    private String supplierId;
    @TableField(exist = false)
    @ApiModelProperty(value = "默认供应商名称")
    private String supplierName;

    @TableField(exist = false)
    @ApiModelProperty("库存")
    private BigDecimal reserveQty;

    @TableField(exist = false)
    private String moId;

    /**
     * 成品skuId
     */
    @TableField(exist = false)
    private String parentSkuId;
}
