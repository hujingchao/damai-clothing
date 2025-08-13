package com.fenglei.model.prd.entity;

import java.util.List;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.UBillItemBaseEntity;
import java.math.BigDecimal;

import com.fenglei.model.basedata.BdMaterialDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 入库单分录
 * </p>
 *
 * @author zgm
 * @since 2024-04-18
 */
@Getter
@Setter
@TableName("prd_in_stock_item")
@ApiModel(value = "PrdInStockItem对象", description = "入库单分录")
public class PrdInStockItem extends UBillItemBaseEntity {

    @ApiModelProperty("物料id")
    private String productId;

    @ApiModelProperty("skuId")
    private String skuId;

    @ApiModelProperty("工票id")
    private String ticketItemId;

    @ApiModelProperty("工票号")
    private String ticketNo;

    @ApiModelProperty("入库数量")
    private BigDecimal InStockNum;

    private String remark;

    @ApiModelProperty("主图id")
    private String mainPicId;

    @ApiModelProperty("主图")
    @TableField(exist = false)
    private String mainPic;

    @ApiModelProperty("置顶")
    private Integer setTop;

    @TableField(exist = false)
    @ApiModelProperty(value = "物料名称")
    private String productName;

    @TableField(exist = false)
    @ApiModelProperty(value = "物料编码")
    private String productNum;

    @ApiModelProperty("理由")
    private String reason;

    @TableField(exist = false)
    @ApiModelProperty(value = "颜色/色号")
    private String color;
    @TableField(exist = false)
    @ApiModelProperty(value = "规格")
    private String specification;

    @TableField(exist = false)
    @ApiModelProperty(value = "属性详细")
    private List<BdMaterialDetail> details;



    @ApiModelProperty("id集合，用于接收前台传参，如批量删除")
    @TableField(exist = false)
    private List<String> ids;
}
