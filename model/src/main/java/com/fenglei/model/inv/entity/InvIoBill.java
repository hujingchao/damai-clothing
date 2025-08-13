package com.fenglei.model.inv.entity;

import java.io.Serializable;
import java.util.List;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.UBillItemBaseEntity;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 物料收发流水账
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-22
 */
@Getter
@Setter
@TableName("inv_io_bill")
@ApiModel(value = "InvIoBill对象", description = "物料收发流水账")
public class InvIoBill implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "内码")
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "物料详情id")
    private String materialDetailId;

    @ApiModelProperty("数量")
    private BigDecimal qty;

    @ApiModelProperty("匹数")
    private BigDecimal piQty;

    @ApiModelProperty("仓库id")
    private String repositoryId;

    @ApiModelProperty("货位Id")
    private String positionId;

    @ApiModelProperty("出库，入库")
    private String io;

    @ApiModelProperty("出入库日期")
    private String bizDate;

    @ApiModelProperty("源单id")
    private String srcId;

    @ApiModelProperty("源单类型")
    private String srcType;

    @ApiModelProperty("源单分录id")
    private String srcItemId;

    @ApiModelProperty("包号")
    private String lot;

    @ApiModelProperty("id集合，用于接收前台传参，如批量删除")
    @TableField(exist = false)
    private List<String> ids;

    @ApiModelProperty("商品id")
    @TableField(exist = false)
    private String productId;
    @ApiModelProperty("商品名称")
    @TableField(exist = false)
    private String productName;
    @ApiModelProperty("商品编码")
    @TableField(exist = false)
    private String productNum;
    @TableField(exist = false)
    @ApiModelProperty(value = "商家编码")
    private String sjNumber;
    @TableField(exist = false)
    @ApiModelProperty(value = "颜色id")
    private String colorId;
    @TableField(exist = false)
    @ApiModelProperty(value = "颜色")
    private String color;
    @TableField(exist = false)
    @ApiModelProperty(value = "规格型号id")
    private String specificationId;
    @TableField(exist = false)
    @ApiModelProperty(value = "规格型号")
    private String specification;


    @TableField(exist = false)
    @ApiModelProperty("分类")
    private String materialGroup;
    @TableField(exist = false)
    @ApiModelProperty("仓库")
    private String repositoryName;
    @TableField(exist = false)
    @ApiModelProperty("仓位")
    private String positionName;
    @TableField(exist = false)
    @ApiModelProperty("分类名称")
    private String materialGroupName;


    @TableField(exist = false)
    @ApiModelProperty(value = "开始日期")
    private String beginDate;
    @TableField(exist = false)
    @ApiModelProperty("结束日期")
    private String endDate;
    @TableField(exist = false)
    @ApiModelProperty("日期范围")
    private List<String> filterDateArr;
}
