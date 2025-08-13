package com.fenglei.model.workFlow.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author yzy
 */
@ApiModel(value = "FlowUser", description = "流程用户类，需要将系统中的user字段映射到上面")
@Data
public class FlowUser implements Serializable {
    private static final long serialVersionUID = 8113097899473367496L;
    @ApiModelProperty(value = "角色ID")
    private List<String> roleIds;
    @ApiModelProperty(value = "是否是审核管理员")
    private Boolean isAuditManager;
    @ApiModelProperty(value = "部门ID")
    private String employeeDepartmentId;
    @ApiModelProperty(value = "部门名称")
    private String departmentName;
    @ApiModelProperty(value = "用户名称")
    private String employeeName;
    private String isLeave="0";
    @ApiModelProperty(value = "用户ID")
    private String id;
    private Boolean open = false;
    private String departmentNames;

}
