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
@ApiModel(value = "BdTempMaterialRaw-物料-原材料", description = "物料-原材料类")
@TableName(value = "bd_temp_material_raw")
public class BdTempMaterialRaw extends BaseItemEntity {

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
    @ApiModelProperty(value = "原材料")
    private BdMaterial rawMaterial;

    @ApiModelProperty(value = "用量(分子)")
    private BigDecimal numerator;

    @ApiModelProperty(value = "用量(分母)")
    private BigDecimal denominator;
}
