package com.fenglei.model.basedata;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * <p>
 * 设备
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-09
 */
@Getter
@Setter
@TableName("bd_equipment")
@ApiModel(value = "BdEquipment对象", description = "设备")
public class BdEquipment extends BaseEntity {

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty(value = "编码")
    private String number;

    @ApiModelProperty("规格型号")
    private String specification;

    @ApiModelProperty("备注")
    private String remark;

    @TableLogic
    private Boolean deleted;


    @ApiModelProperty("id集合，用于接收前台传参，如批量删除")
    @TableField(exist = false)
    private List<String> ids;
}
