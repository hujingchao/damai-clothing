package com.fenglei.model.basedata;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.BaseItemEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ljw
 */
@Data
@ApiModel(value = "BdMaterialAux-物料-辅料", description = "物料-辅料类")
@TableName(value = "bd_material_aux")
public class BdMaterialAux extends BaseItemEntity {

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
    @ApiModelProperty(value = "辅料")
    private BdMaterial auxMaterial;

    @ApiModelProperty(value = "用量(分子)")
    private BigDecimal numerator;

    @ApiModelProperty(value = "用量(分母)")
    private BigDecimal denominator;

    @ApiModelProperty("逻辑删除标识 0-未删除 1-已删除")
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;
}
