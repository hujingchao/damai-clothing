package com.fenglei.model.basedata;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.BaseItemEntity;
import com.fenglei.model.system.entity.SysUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ljw
 */
@Data
@ApiModel(value = "BdTempMaterialRouteSpecial-物料-工艺路线-特殊工价", description = "物料-工艺路线-特殊工价类")
@TableName(value = "bd_temp_material_route_special")
public class BdTempMaterialRouteSpecial extends BaseItemEntity {

    @ApiModelProperty(value = "员工id")
    private String staffId;

    @TableField(exist = false)
    @ApiModelProperty(value = "员工")
    private SysUser staff;

    @ApiModelProperty(value = "工价(元)")
    private BigDecimal price;

    @ApiModelProperty(value = "备注")
    private String remark;
}
