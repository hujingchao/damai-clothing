package com.fenglei.model.workFlow.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.BaseFlowPojo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * User: yzy
 * Date: 2019/9/24 0024
 * Time: 11:03
 * Description: No Description
 * @author yzy
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "TableInfoItem", description = "表单信息子类")
@Data
@TableName(value = "sys_table_info_item")
public class TableInfoItem extends BaseFlowPojo {
    private static final long serialVersionUID = -948818803777756894L;
    @ApiModelProperty(value = "字段代码")
    private String fieldCode;
    @ApiModelProperty(value = "字段名称")
    private String fieldName;
    @ApiModelProperty(value = "字段类型")
    private String fieldType;
    @ApiModelProperty(value = "主表ID")
    private String tableInfoId;
    @ApiModelProperty(value = "是否用于流程")
    private Boolean isCanUseInProcess;
    @ApiModelProperty(value = "是否是审批条件")
    private Boolean isItAnApprovalCondition;
}
