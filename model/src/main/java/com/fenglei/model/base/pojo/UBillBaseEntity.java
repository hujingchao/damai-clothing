package com.fenglei.model.base.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fenglei.model.workFlow.entity.ChildNode;
import com.fenglei.model.workFlow.entity.ChildNodeApprovalResult;
import com.fenglei.model.workFlow.entity.WorkFlow;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UBillBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty("内码")
    private String id;

    @ApiModelProperty("编号")
    private String no;

    @TableField(exist = false)
    @ApiModelProperty("编号，用于过滤")
    private String noLike;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间")
    private String createTime;

    @TableField(exist = false)
    @ApiModelProperty("创建人")
    private String creator;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建人id")
    private String creatorId;

    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty("更新时间")
    private String updateTime;

    @TableField(exist = false)
    @ApiModelProperty("更新人")
    private String updater;

    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty("修改人id")
    private String updaterId;

    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty("审核时间")
    private String auditTime;

    @TableField(exist = false)
    @ApiModelProperty("审核人")
    private String auditor;

    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty("修改人id")
    private String auditorId;

    @TableField(exist = false)
    @ApiModelProperty("审核时间(开始)，用于过滤")
    private String auditTimeBegin;
    @TableField(exist = false)
    @ApiModelProperty("审核时间(结束)，用于过滤")
    private String auditTimeEnd;

    @ApiModelProperty(value = "状态：0-未提交；1-流转中；2-已完成；3-重新提交")
    private Integer billStatus;
    @TableField(exist = false)
    private List<Integer> inBillStatus;

    @TableField(exist = false)
    @ApiModelProperty("批量id")
    private List<String> ids;

    @TableField(exist = false)
    @ApiModelProperty(value = "排除项ids")
    private List<String> excludeIds;


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
}

