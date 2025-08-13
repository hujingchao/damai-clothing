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
@ApiModel(value = "BdMaterialSupplier-物料-供应商", description = "物料-供应商类")
@TableName(value = "bd_material_supplier")
public class BdMaterialSupplier extends BaseItemEntity {

    @ApiModelProperty(value = "供应商id")
    private String supplierId;

    @TableField(exist = false)
    @ApiModelProperty(value = "供应商")
    private BdSupplier bdSupplier;

    @ApiModelProperty(value = "是否默认供应商")
    private Boolean isDefault;

    @ApiModelProperty(value = "单价(元)")
    private BigDecimal price;

    @ApiModelProperty("逻辑删除标识 0-未删除 1-已删除")
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;
}
