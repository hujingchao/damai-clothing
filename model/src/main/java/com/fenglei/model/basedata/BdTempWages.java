package com.fenglei.model.basedata;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.BaseEntity;
import com.fenglei.model.workFlow.entity.ChildNode;
import com.fenglei.model.workFlow.entity.ChildNodeApprovalResult;
import com.fenglei.model.workFlow.entity.WorkFlow;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author ljw
 */
@Data
@ApiModel(value = "BdTempWages-工价临时", description = "工价临时类")
@TableName(value = "bd_temp_wages")
public class BdTempWages extends BaseEntity {

    @ApiModelProperty(value = "编码")
    private String number;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "备注")
    @TableField(fill = FieldFill.UPDATE)
    private String remark;

    @ApiModelProperty(value = "状态：0-未提交；1-流转中；2-已完成；3-重新提交")
    private Integer status;

    @ApiModelProperty(value = "最终审核时间")
    @TableField(fill = FieldFill.UPDATE)
    private String auditTime;

    @TableField(exist = false)
    @ApiModelProperty(value = "最终审核人姓名")
    private String auditor;

    @ApiModelProperty(value = "最终审核人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String auditorId;

    @ApiModelProperty(value = "禁用状态：0-禁用；1-启用")
    private Boolean enabled;

    @ApiModelProperty(value = "启用禁止时间")
    @TableField(fill = FieldFill.UPDATE)
    private String enableTime;

    @TableField(exist = false)
    @ApiModelProperty(value = "启用禁止人姓名")
    private String enabler;

    @ApiModelProperty(value = "启用禁止人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String enablerId;

    @TableField(exist = false)
    @ApiModelProperty(value = "工价明细")
    private List<BdTempWagesItem> bdWagesItems;

    @TableField(exist = false)
    @ApiModelProperty(value = "工序id")
    private String procedureId;

    @ApiModelProperty("节点审批条件")
    @TableField(exist = false)
    private ChildNodeApprovalResult childNodeApprovalResult;
    @ApiModelProperty("流程ID")
    @TableField(exist = false)
    private String workFlowId;
    @ApiModelProperty("流程配置类")
    @TableField(exist = false)
    private WorkFlow workFlow;
    @ApiModelProperty("当前节点")
    @TableField(exist = false)
    private String currentNodeId;
    @ApiModelProperty("抄送节点")
    @TableField(exist = false)
    private List<ChildNode> childNodes;
    @ApiModelProperty("流程实例状态")
    @TableField(exist = false)
    private Integer workFlowInstantiateStatus;
    @ApiModelProperty("发起人id")
    @TableField(exist = false)
    private String userId;
    @TableField(exist = false)
    private Integer nodeStatus;

    @ApiModelProperty("原始id")
    private String originalId;
}
