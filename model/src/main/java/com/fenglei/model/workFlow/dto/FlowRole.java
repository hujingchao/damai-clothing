package com.fenglei.model.workFlow.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yzy
 */
@ApiModel(value = "FlowRole", description = "流程角色类，需要将系统中的Role表中相关字段映射到上面")
@Data
public class FlowRole implements Serializable {
    private static final long serialVersionUID = 203558367109909517L;
    @ApiModelProperty(value = "角色Id")
    private String roleId;
    private String code = "";
    private String scope = "1";
    @ApiModelProperty(value = "角色名")
    private String roleName;
    private String description;
    @ApiModelProperty(value = "状态")
    private String status = "1";

}
