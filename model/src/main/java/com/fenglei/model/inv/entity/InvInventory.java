package com.fenglei.model.inv.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.BaseEntity;
import com.fenglei.model.basedata.BdMaterial;
import com.fenglei.model.basedata.BdSupplier;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ljw
 */
@Data
@ApiModel(value = "InvInventory-即时库存", description = "即时库存类")
@TableName(value = "inv_inventory")
public class InvInventory extends BaseEntity {

    @ApiModelProperty(value = "物料详情id")
    private String materialDetailId;

    @TableField(exist = false)
    @ApiModelProperty(value = "物料详情ids")
    private List<String> materialDetailIds;

    @TableField(exist = false)
    private List<Integer> inMaterialGroup;

    @TableField(exist = false)
    private String materialGroupId;

    @TableField(exist = false)
    @ApiModelProperty(value = "存货档案id")
    private String productId;
    @TableField(exist = false)
    @ApiModelProperty(value = "存货档案ids")
    private List<String> productIds;

    @TableField(exist = false)
    @ApiModelProperty(value = "颜色/色号id")
    private String colorId;
    @TableField(exist = false)
    @ApiModelProperty(value = "颜色/色号")
    private String color;
    @TableField(exist = false)
    @ApiModelProperty(value = "颜色/色号 模糊搜索")
    private String colorLike;

    @TableField(exist = false)
    @ApiModelProperty(value = "规格id")
    private String specificationId;
    @TableField(exist = false)
    @ApiModelProperty(value = "规格")
    private String specification;
    @TableField(exist = false)
    @ApiModelProperty(value = "规格 模糊搜索")
    private String specificationLike;

    @TableField(exist = false)
    @ApiModelProperty(value = "存货档案")
    private BdMaterial material;

    @ApiModelProperty("数量")
    private BigDecimal qty;

    @ApiModelProperty("总数量")
    @TableField(exist = false)
    private BigDecimal lotQty;

    @ApiModelProperty("匹数")
    private BigDecimal piQty;

    @ApiModelProperty(value = "仓库id")
    private String repositoryId;
    @TableField(exist = false)
    @ApiModelProperty(value = "仓库名称")
    private String repositoryName;
    @TableField(exist = false)
    @ApiModelProperty(value = "仓库ids")
    private List<String> repositoryIds;

    @ApiModelProperty(value = "货位id")
    private String positionId;
    @TableField(exist = false)
    @ApiModelProperty(value = "货位名称")
    private String positionName;
    @TableField(exist = false)
    @ApiModelProperty(value = "货位ids")
    private List<String> positionIds;

    @ApiModelProperty("单价(元)")
    private BigDecimal price;

    @ApiModelProperty("批号")
    private String lot;
    @TableField(exist = false)
    @ApiModelProperty(value = "批号 模糊搜索")
    private String lotLike;

    @ApiModelProperty("逻辑删除标识 0-未删除 1-已删除")
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;

    @TableField(exist = false)
    private List<BdMaterial> materials;
}
