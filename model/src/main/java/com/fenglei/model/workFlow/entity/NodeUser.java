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
@ApiModel(value = "NodeUser", description = "流程操作用户类")
@Data
@TableName(value = "flow_node_user")
public class NodeUser extends BaseFlowPojo {
    private static final long serialVersionUID = -3582985668021211195L;
    @ApiModelProperty(value = "目标Id")
    private String targetId;
    @ApiModelProperty(value = "用户类型，1.指向用户 2.角色")
    private String type;
    @ApiModelProperty(value = "用户名")
    private String name;
    @ApiModelProperty(value = "节点ID")
    private String childNodeId;
    @ApiModelProperty(value = "流程ID")
    private String workFlowInstantiateId;
    @ApiModelProperty(value = "排序需要")
    private Integer sortIndex;
}
