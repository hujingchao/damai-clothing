package com.fenglei.model.pur.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.UBillItemBaseEntity;
import com.fenglei.model.basedata.BdMaterial;
import com.fenglei.model.basedata.BdSupplier;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "PurPurchaseInstockItem-采购入库单 明细详情", description = "采购入库单 明细详情")
@TableName(value = "pur_purchase_instock_item")
public class PurPurchaseInstockItem extends UBillItemBaseEntity {

    @ApiModelProperty(value = "源单明细id")
    private String srcItemId;

    @ApiModelProperty(value = "物料详情id")
    private String materialDetailId;

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
    @ApiModelProperty(value = "原材料/辅料")
    private BdMaterial material;

    @ApiModelProperty("供应商id")
    private String supplierId;
    @TableField(exist = false)
    @ApiModelProperty(value = "供应商名称")
    private BdSupplier supplier;

    @ApiModelProperty("数量")
    private BigDecimal qty;
    @ApiModelProperty("单价")
    private BigDecimal price;

    @ApiModelProperty("匹数")
    private BigDecimal piQty;

    @ApiModelProperty(value = "仓库id")
    private String repositoryId;
    @TableField(exist = false)
    @ApiModelProperty(value = "仓库名称")
    private String repositoryName;
    @TableField(exist = false)
    private Boolean usePosition;

    @ApiModelProperty(value = "货位id")
    private String positionId;
    @TableField(exist = false)
    @ApiModelProperty(value = "货位名称")
    private String positionName;

    @ApiModelProperty("备注")
    private String remark;


    @ApiModelProperty("入库日期")
    @TableField(exist = false)
    private String bizDate;
}
