package com.fenglei.model.prd.entity;

import java.math.BigDecimal;
import java.util.List;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.UBillItemBaseEntity;
import com.fenglei.model.basedata.BdMaterialDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 包装单分录
 * </p>
 *
 * @author zgm
 * @since 2024-04-11
 */
@Getter
@Setter
@TableName("prd_packing_item")
@ApiModel(value = "PrdPackingItem对象", description = "包装单分录")
public class PrdPackingItem extends UBillItemBaseEntity {

    @ApiModelProperty("物料id")
    private String productId;

    @ApiModelProperty("工票id")
    private String ticketItemId;

    @ApiModelProperty("工票号")
    private String ticketNo;

    @ApiModelProperty("包装数量")
    private BigDecimal packNum;

    private String remark;

    @ApiModelProperty("主图id")
    private String mainPicId;

    @ApiModelProperty("主图")
    @TableField(exist = false)
    private String mainPic;


    @ApiModelProperty("置顶 默认0  置顶1")
    private Integer setTop;


    @TableField(exist = false)
    @ApiModelProperty(value = "物料名称")
    private String productName;

    @TableField(exist = false)
    @ApiModelProperty(value = "物料编码")
    private String productNum;



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
