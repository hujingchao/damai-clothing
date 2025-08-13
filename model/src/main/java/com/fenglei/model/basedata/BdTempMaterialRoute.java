package com.fenglei.model.basedata;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.BaseItemEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ljw
 */
@Data
@ApiModel(value = "BdTempMaterialRoute-物料-工艺路线", description = "物料-工艺路线类")
@TableName(value = "bd_temp_material_route")
public class BdTempMaterialRoute extends BaseItemEntity {

    @ApiModelProperty("工序号")
    private Integer no;

    @ApiModelProperty(value = "工序id")
    private String procedureId;

    @TableField(exist = false)
    @ApiModelProperty(value = "工序")
    private BdProcedure bdProcedure;

    @ApiModelProperty(value = "工价(元)")
    private BigDecimal price;

    @TableField(exist = false)
    private List<BdTempMaterialRouteSpecial> routeSpecials;
}
