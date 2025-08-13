package com.fenglei.model.inv.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.UBillBaseEntity;
import com.fenglei.model.base.pojo.UBillItemBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
/**
 * <p>
 *其他入库单分录
 * </p>
 *
 * @author zgm
 * @since 2024-04-28
 */
@Getter
@Setter
@TableName("inv_other_in_stock_item")
@ApiModel(value = "InvOtherInStockItem对象", description = "其他入库单分录")
public class InvOtherInStockItem  extends UBillItemBaseEntity {


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

    //货物信息
    @ApiModelProperty("商品编码")
    @TableField(exist = false)
    private String materialId;

    @ApiModelProperty("商品详情")
    private String materialDetailId;

    @ApiModelProperty("商品编码")
    @TableField(exist = false)
    private String materialNumber;

    @ApiModelProperty("商品名称")
    @TableField(exist = false)
    private String materialName;

    @ApiModelProperty("商品类型")
    @TableField(exist = false)
    private String materialGroup;

    @ApiModelProperty("单位")
    @TableField(exist = false)
    private String unitId;

    @ApiModelProperty("单位")
    @TableField(exist = false)
    private String unitName;

    @ApiModelProperty("主图")
    @TableField(exist = false)
    private String mainPicId;

    @ApiModelProperty("仓位")
    @TableField(exist = false)
    private String mainPicUrl;

    @ApiModelProperty("颜色")
    @TableField(exist = false)
    private String colorId;

    @ApiModelProperty("颜色")
    @TableField(exist = false)
    private String color;

    @ApiModelProperty("规格")
    @TableField(exist = false)
    private String specificationId;

    @ApiModelProperty("规格")
    @TableField(exist = false)
    private String specification;

    @ApiModelProperty("库存数量")
    @TableField(exist = false)
    private BigDecimal invQty;

    @ApiModelProperty("数量")
    private BigDecimal inQty;

    @ApiModelProperty("库存匹数")
    @TableField(exist = false)
    private BigDecimal invPiQty;

    @ApiModelProperty("数量")
    private BigDecimal inPiQty;

    @ApiModelProperty("价格")
    private BigDecimal price;

    @ApiModelProperty("备注")
    private String remark;





    @ApiModelProperty("id集合，用于接收前台传参，如批量删除")
    @TableField(exist = false)
    private List<String> ids;
}
