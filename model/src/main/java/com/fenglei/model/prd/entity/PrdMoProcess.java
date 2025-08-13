package com.fenglei.model.prd.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.UBillItemBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ljw
 */
@Data
@ApiModel(value = "PrdMoProcess-生产订单 生产工序", description = "生产订单 生产工序类")
@TableName(value = "prd_mo_process")
public class PrdMoProcess extends UBillItemBaseEntity {


    @ApiModelProperty("工序号")
    private Integer no;

    @ApiModelProperty(value = "工序id")
    private String procedureId;

    @TableField(exist = false)
    @ApiModelProperty(value = "工序编码")
    private String procedureNumber;

    @TableField(exist = false)
    @ApiModelProperty(value = "工序名称")
    private String procedureName;

    @TableField(exist = false)
    @ApiModelProperty(value = "工序描述")
    private String procedureRemark;

    @ApiModelProperty(value = "工价")
    private BigDecimal price;

    @ApiModelProperty(value = "数量")
    private BigDecimal qty;

    @ApiModelProperty(value = "负责部门id")
    private String departmentId;

    @TableField(exist = false)
    @ApiModelProperty(value = "负责部门名称")
    private String departmentName;

    @ApiModelProperty(value = "备注")
    @TableField(fill = FieldFill.UPDATE)
    private String remark;
}
