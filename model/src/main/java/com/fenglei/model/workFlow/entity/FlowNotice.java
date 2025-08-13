package com.fenglei.model.workFlow.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
@ApiModel(value = "FlowNotice", description = "流程通知类")
@Data
@TableName(value = "flow_flow_notice")
public class FlowNotice extends BaseFlowPojo {
    private static final long serialVersionUID = -313727147291791111L;
    @ApiModelProperty(value = "通知内容")
    private String content;
    @ApiModelProperty(value = "单据Id")
    private String formId;
    @ApiModelProperty(value = "通知人角色ID（id之间用‘，’隔开）")
    private String roleIds;
    @ApiModelProperty(value = "通知人ID（id之间用‘，’隔开）")
    private String userIds;
    @ApiModelProperty(value = "通知类型，1.审批 2.抄送")
    private String noticeType;
    @ApiModelProperty(value = "通知时间")
    private String noticeTime;
    @ApiModelProperty(value = "是否阅读")
    private Boolean isRead;
    @ApiModelProperty(value = "流水号")
    @TableField(exist = false)
    private String serialNumber;
}
