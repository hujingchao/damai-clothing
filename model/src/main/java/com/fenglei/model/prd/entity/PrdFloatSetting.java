package com.fenglei.model.prd.entity;

import java.util.List;

import com.baomidou.mybatisplus.annotation.*;
import com.fenglei.model.base.pojo.UBillBaseEntity;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author zgm
 * @since 2024-04-18
 */
@Getter
@Setter
@TableName("prd_float_setting")
@ApiModel(value = "PrdFloatSetting对象", description = "")
public class PrdFloatSetting  {

    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty("内码")
    private String id;


    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间")
    private String createTime;

    @TableField(exist = false)
    @ApiModelProperty("创建人")
    private String creator;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建人id")
    private String creatorId;

    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty("更新时间")
    private String updateTime;

    @TableField(exist = false)
    @ApiModelProperty("更新人")
    private String updater;

    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty("修改人id")
    private String updaterId;

    @ApiModelProperty("计划下浮数量(入库)")
    private BigDecimal planDown;

    @ApiModelProperty("计划上浮数量(入库)")
    private BigDecimal planUp;

    @ApiModelProperty("生产下浮数量(入库)")
    private BigDecimal prdDown;

    @ApiModelProperty("生产上浮数量(入库)")
    private BigDecimal prdUp;

    @ApiModelProperty("计划下浮数量(裁床)")
    private BigDecimal planDownCut;

    @ApiModelProperty("计划上浮数量(裁床)")
    private BigDecimal planUpCut;

    @ApiModelProperty("生产下浮数量(裁床)")
    private BigDecimal prdDownCut;

    @ApiModelProperty("生产上浮数量(裁床)")
    private BigDecimal prdUpCut;



    @ApiModelProperty("id集合，用于接收前台传参，如批量删除")
    @TableField(exist = false)
    private List<String> ids;
}
