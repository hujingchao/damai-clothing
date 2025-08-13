package com.fenglei.model.oms.entity;

import java.util.List;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;

import com.fenglei.model.base.pojo.UBillItemBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 销售出库分录明细
 * </p>
 *
 * @author zjn
 * @since 2024-04-15
 */
@Getter
@Setter
@TableName("oms_sale_out_stock_item_detail")
@ApiModel(value = "OmsSaleOutStockItemDetail对象", description = "销售出库分录明细")
public class OmsSaleOutStockItemDetail extends UBillItemBaseEntity {

    @ApiModelProperty("销售出库单id")
    private String gpId;

    @ApiModelProperty("包号（批号）")
    private String lot;

    @ApiModelProperty("仓库")
    private String repositoryId;

    @ApiModelProperty("仓库名称")
    @TableField(exist = false)
    private String repositoryName;

    @ApiModelProperty("仓位")
    private String positionId;

    @ApiModelProperty("仓位")
    @TableField(exist = false)
    private String positionName;

    @ApiModelProperty("数量")
    private BigDecimal qty;

    @ApiModelProperty("出库数量")
    private BigDecimal outStockQty;

    @ApiModelProperty("卷数")
    private BigDecimal piQty;

    @ApiModelProperty("成本价")
    private BigDecimal price;

    @ApiModelProperty("物料详情id")
    private String materialDetailId;

    @TableField(exist = false)
    private String materialNumber;
    @TableField(exist = false)
    private String materialName;
    @TableField(exist = false)
    private String materialGroup;
    @TableField(exist = false)
    private String unitName;
    @TableField(exist = false)
    private String mainPicUrl;
    @TableField(exist = false)
    private String color;
    @TableField(exist = false)
    private String specification;



    @ApiModelProperty("id集合，用于接收前台传参，如批量删除")
    @TableField(exist = false)
    private List<String> ids;
}
