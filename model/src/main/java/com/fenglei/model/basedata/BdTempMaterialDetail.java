package com.fenglei.model.basedata;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.BaseItemEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ljw
 */
@Data
@ApiModel(value = "BdTempMaterialDetail-物料详细", description = "物料详细类")
@TableName(value = "bd_temp_material_detail")
public class BdTempMaterialDetail extends BaseItemEntity {

    @ApiModelProperty(value = "商家编码")
    private String number;

    @ApiModelProperty(value = "颜色/色号id")
    private String colorId;

    @TableField(exist = false)
    @ApiModelProperty(value = "颜色/色号")
    private String color;

    @ApiModelProperty(value = "规格id")
    private String specificationId;

    @TableField(exist = false)
    @ApiModelProperty(value = "规格")
    private String specification;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private String createTime;
    /**
     * 创建人
     */
    @TableField(exist = false)
    private String creator;
    /**
     * 创建人id
     */
    @TableField(fill = FieldFill.INSERT)
    private String creatorId;

    @ApiModelProperty("原始id")
    private String originalId;

    @ApiModelProperty(value = "状态：0-未提交；1-流转中；2-已完成；3-重新提交")
    private Integer status;
}
