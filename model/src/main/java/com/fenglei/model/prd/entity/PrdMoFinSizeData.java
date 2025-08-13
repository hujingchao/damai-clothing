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
@ApiModel(value = "PrdMoFinSizeData-生产订单 成品尺寸", description = "生产订单 成品尺寸类")
@TableName(value = "prd_mo_fin_size_data")
public class PrdMoFinSizeData extends UBillItemBaseEntity {

    @ApiModelProperty(value = "第几行")
    private Integer rowIdx;

    @ApiModelProperty(value = "列名")
    private String columnName;

    @ApiModelProperty("值")
    private String value;
}
