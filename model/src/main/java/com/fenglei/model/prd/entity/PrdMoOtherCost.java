package com.fenglei.model.prd.entity;

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
@ApiModel(value = "PrdMoOtherCost-生产订单 其他成本", description = "生产订单 其他成本类")
@TableName(value = "prd_mo_other_cost")
public class PrdMoOtherCost extends UBillItemBaseEntity {

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty("金额")
    private BigDecimal amount;
}
