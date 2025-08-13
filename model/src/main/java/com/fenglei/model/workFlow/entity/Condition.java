package com.fenglei.model.workFlow.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.BaseFlowPojo;
import com.fenglei.model.workFlow.dto.Option;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author yzy
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Condition", description = "流程条件类")
@Data
@TableName(value = "flow_condition")
public class Condition extends BaseFlowPojo {
    private static final long serialVersionUID = -8085661668895790622L;
    @ApiModelProperty(value = "列Id")
    private String columnId;
    @ApiModelProperty(value = "列名称")
    private String columnName;
    @ApiModelProperty(value = "节点类型")
    private String type;
    @ApiModelProperty(value = "节点英文条件")
    private String conditionEn;
    @ApiModelProperty(value = "节点中文条件")
    private String conditionCn;
    @ApiModelProperty(value = "选项类型：1.小于 2.大于 3.小于等于 4.等于 5.大于等于 6.介于两个数之间")
    private String optType;
    @ApiModelProperty(value = "左边符号1")
    private String zdy1;
    @ApiModelProperty(value = "左边符号2")
    private String zdy2;
    @ApiModelProperty(value = "右边符号1")
    private String opt1;
    @ApiModelProperty(value = "右边符号2")
    private String opt2;
    @ApiModelProperty(value = "数据库字段名称")
    private String columnDbname;
    @ApiModelProperty(value = "数据库字段类型")
    private String columnType;
    @ApiModelProperty(value = "展示类型")
    private String showType;
    @ApiModelProperty(value = "展示名称")
    private String showName;
    @ApiModelProperty(value = "固定下框值")
    private String fixedDownBoxValue;
    @ApiModelProperty(value = "流程ID")
    private String workFlowInstantiateId;
    @ApiModelProperty(value = "子节点ID")
    private String childNodeId;
    @ApiModelProperty(value = "表单ID")
    private String tableId;
    @ApiModelProperty(value = "选项")
    @TableField(exist = false)
    private List<Option> options;
}
