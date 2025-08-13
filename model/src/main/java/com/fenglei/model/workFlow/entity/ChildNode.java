package com.fenglei.model.workFlow.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.BaseFlowPojo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yzy
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ChildNode", description = "流程节点类")
@Data
@TableName(value = "flow_child_node")
public class ChildNode extends BaseFlowPojo {
    private static final long serialVersionUID = 1552296232781228445L;
    @ApiModelProperty(value = "节点名称")
    private String nodeName;
    @ApiModelProperty(value = "节点类别--》0.发起 1.审核 2.抄送 3.条件 4.路由")
    private String type;
    @ApiModelProperty(value = "优先级，数值越小优先级越高")
    private String priorityLevel;
    @ApiModelProperty(value = "设置类型 1.指定成员 2.主管 3.角色 4.发起人自选 5.发起人自己 7.连续多级主管")
    private String settype;
    @ApiModelProperty(value = "选择模式 1.选择一个人 2.选择多个人")
    private String selectMode;
    @ApiModelProperty(value = "选择范围 1.全公司 2.指定成员 3.指点角色")
    private String selectRange;
    @ApiModelProperty(value ="检查角色Id")
    private String examineRoleId;
    @ApiModelProperty(value ="主管级别 1.直接主管 2.2级主管 3.3级主管 4.4级主管")
    private String directorLevel;
    @ApiModelProperty(value = "替换为向上")
    private String replaceByUp;
    @ApiModelProperty(value = "多人审批时采用的审批方式 1.依次审批 2.会签（须所有审批人同意）")
    private String examineMode;
    @ApiModelProperty(value = "审批人为空时 1.自动审批通过/不允许发起 2.转交给审核管理员")
    private String noHanderAction;
    @ApiModelProperty(value = "检查端点类型")
    private String examineEndType;
    @ApiModelProperty(value = "检查端角色ID")
    private String examineEndRoleId;
    @ApiModelProperty(value = "连续多级主管审批时，最终主管级别 1.最高级 2.第二级 3.第三级 4，第四级")
    private String examineEndDirectorLevel;
    @ApiModelProperty(value = "允许发起人自选抄送人，当节点为抄送时有")
    private String ccSelfSelectFlag;
    @ApiModelProperty(value = "错误")
    private Boolean error;
    @ApiModelProperty(value = "条件列表")
    @TableField(exist = false)
    private List<Condition> conditionList = new ArrayList<>();
    @ApiModelProperty(value = "节点用户列表")
    @TableField(exist = false)
    private List<NodeUser> nodeUserList = new ArrayList<>();
    @ApiModelProperty(value = "子节点")
    @TableField(exist = false)
    private ChildNode childNode;
    @ApiModelProperty(value = "条件节点")
    @TableField(exist = false)
    private List<ChildNode> conditionNodes = new ArrayList<>();
    @ApiModelProperty(value = "当上级节点不是路由时，本节点是普通节点填写，父节点ID")
    private String childNodeId;
    @ApiModelProperty(value = "当上级节点是路由时，本节点是条件节点填写，父节点ID")
    private String pid;
    @ApiModelProperty(value = "流程ID")
    private String workFlowInstantiateId;
    @ApiModelProperty(value = "节点状态：0.未到达 1.当前节点（只有节点是审核节点时存在此状态） 2.通过节点（当节点是路由节点时默认为通过状态） 3.驳回节点 ")
    private Integer status;
    @ApiModelProperty(value = "审核结果")
    @TableField(exist = false)
    private List<ChildNodeApprovalResult> childNodeApprovalResults;
    @TableField(exist = false)
    @ApiModelProperty(value = "单据ID")
    private String fromId;
    @ApiModelProperty(value = "流程实例")
    @TableField(exist = false)
    private Integer workFlowInstantiateStatus;
    @ApiModelProperty(value = "发起人姓名")
    @TableField(exist = false)
    private String initiatorName;
    @ApiModelProperty(value = "节点用户ID集合")
    @TableField(exist = false)
    private List<String> nodeUserIds;
}
