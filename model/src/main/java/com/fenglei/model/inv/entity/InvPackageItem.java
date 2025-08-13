package com.fenglei.model.inv.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.UBillItemBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 入库后打包子表
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-23
 */
@Getter
@Setter
@TableName("inv_package_item")
@ApiModel(value = "InvPackageItem对象", description = "入库后打包子表")
public class InvPackageItem extends UBillItemBaseEntity {

    @ApiModelProperty("库存id")
    private String invId;

    @ApiModelProperty("物料详情id")
    private String materialDetailId;

    @ApiModelProperty("库存数")
    private BigDecimal allQty;

    @ApiModelProperty("包装使用数量")
    private BigDecimal usedQty;

    @ApiModelProperty("每包数量")
    private BigDecimal perQty;

    @ApiModelProperty("包数")
    private Integer count;

    @ApiModelProperty("单价(元)")
    private BigDecimal price;

    @ApiModelProperty("仓库id")
    private String repositoryId;

    @ApiModelProperty("货位Id")
    private String positionId;


    @ApiModelProperty("id集合，用于接收前台传参，如批量删除")
    @TableField(exist = false)
    private List<String> ids;

    @TableField(exist = false)
    @ApiModelProperty("大货款号，id")
    private String productId;
    @TableField(exist = false)
    @ApiModelProperty("大货款号，编码")
    private String productNum;
    @TableField(exist = false)
    @ApiModelProperty("大货款号，名称")
    private String productName;
    @TableField(exist = false)
    @ApiModelProperty("主图")
    private String mainPic;
    @TableField(exist = false)
    @ApiModelProperty("颜色")
    private String color;
    @TableField(exist = false)
    @ApiModelProperty(value = "规格")
    private String specification;
    @TableField(exist = false)
    @ApiModelProperty(value = "单位id")
    private String unitId;
    @TableField(exist = false)
    @ApiModelProperty(value = "单位名称")
    private String unitName;
    @TableField(exist = false)
    @ApiModelProperty("仓库")
    private String repositoryName;
    @TableField(exist = false)
    @ApiModelProperty("货位")
    private String positionName;

    @TableField(exist = false)
    @ApiModelProperty("单号")
    private String packNo;

    @TableField(exist = false)
    @ApiModelProperty("单号")
    private String packTime;

    @TableField(exist = false)
    @ApiModelProperty("单号")
    private String packer;

    @TableField(exist = false)
    @ApiModelProperty("单据状态")
    private String billStatus;


}
