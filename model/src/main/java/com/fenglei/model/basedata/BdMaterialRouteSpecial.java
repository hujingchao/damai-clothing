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
@ApiModel(value = "BdMaterialRouteSpecial-物料-工艺路线-特殊工价", description = "物料-工艺路线-特殊工价类")
@TableName(value = "bd_material_route_special")
public class BdMaterialRouteSpecial extends BaseItemEntity {

    @ApiModelProperty(value = "员工id")
    private String staffId;

    @TableField(exist = false)
    @ApiModelProperty(value = "员工")
    private SysUser staff;

    @ApiModelProperty(value = "工价(元)")
    private BigDecimal price;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty("逻辑删除标识 0-未删除 1-已删除")
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;
}
