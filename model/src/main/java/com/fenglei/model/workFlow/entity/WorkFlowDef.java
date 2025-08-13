package com.fenglei.model.workFlow.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.BaseFlowPojo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yzy
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "WorkFlowDef", description = "流程信息类")
@Data
@TableName(value = "flow_work_flow_def")
public class WorkFlowDef extends BaseFlowPojo {
    private static final long serialVersionUID = 4379038302196535076L;
    @ApiModelProperty(value = "节点信息名称")
    private String name;
    @ApiModelProperty(value = "公共标志")
    private String publicFlag;
    @ApiModelProperty(value = "排序号")
    private String sortNo;
    @ApiModelProperty(value = "重复删除标志")
    private String duplicateRemovelFlag;
    @ApiModelProperty(value = "设置提醒")
    private String optionTip;
    @ApiModelProperty(value = "设置不为空")
    private String optionNotNull;
    @ApiModelProperty(value = "状态")
    private String status;
    @ApiModelProperty(value = "流程ID")
    private String workFlowInstantiateId;
}
