package com.fenglei.model.prd.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.UBillItemBaseEntity;
import com.fenglei.model.basedata.BdMaterial;
import com.fenglei.model.basedata.BdSupplier;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ljw
 */
@Data
@ApiModel(value = "PrdMoSecondaryProcess-生产订单 二次工艺", description = "生产订单 二次工艺类")
@TableName(value = "prd_mo_secondary_process")
public class PrdMoSecondaryProcess extends UBillItemBaseEntity {

    @ApiModelProperty(value = "工序id")
    private String processId;
    @TableField(exist = false)
    @ApiModelProperty(value = "工序名称")
    private String processName;

    @ApiModelProperty("工艺")
    private String technique;

    @ApiModelProperty(value = "数量")
    private BigDecimal quantity;

    @ApiModelProperty(value = "单价")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "使用部位")
    private String usedPart;

    @ApiModelProperty(value = "供应商id")
    private String supplierId;

    @TableField(exist = false)
    @ApiModelProperty(value = "供应商")
    private BdSupplier bdSupplier;

    @ApiModelProperty(value = "备注")
    private String remark;
}
