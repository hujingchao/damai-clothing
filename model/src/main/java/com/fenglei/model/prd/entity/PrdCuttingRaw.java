package com.fenglei.model.prd.entity;

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
 * 裁床单 - 原材料信息
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-11
 */
@Getter
@Setter
@TableName("prd_cutting_raw")
@ApiModel(value = "PrdCuttingRaw对象", description = "裁床单 - 原材料信息")
public class PrdCuttingRaw extends UBillItemBaseEntity {

    @ApiModelProperty("原材料id")
    private String materialDetailId;

    @ApiModelProperty("用量（分子 - KG）")
    private BigDecimal numerator;

    @ApiModelProperty("用量（分母 - KG）")
    private BigDecimal denominator;

    @ApiModelProperty("理论用量（KG）")
    private BigDecimal theoryQty;

    @ApiModelProperty("实际用量（KG）")
    private BigDecimal realQty;

    @ApiModelProperty("实际用量（匹数）")
    private BigDecimal realPiQty;

    @ApiModelProperty("是否使用废料")
    private Boolean useWaste;



    @ApiModelProperty("id集合，用于接收前台传参，如批量删除")
    @TableField(exist = false)
    private List<String> ids;


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
    @ApiModelProperty("单位")
    private String unitName;

    @TableField(exist = false)
    @ApiModelProperty("颜色")
    private String color;
    @TableField(exist = false)
    @ApiModelProperty("规格尺码")
    private String specification;

    @ApiModelProperty("成品SkuId")
    private String parentSkuId;
}
