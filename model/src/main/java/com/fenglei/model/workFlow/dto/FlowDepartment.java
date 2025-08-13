package com.fenglei.model.workFlow.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yzy
 */
@ApiModel(value = "FlowDepartment", description = "流程部门类，需要将系统中的Department表中相关字段映射到上面")
@Data
public class FlowDepartment implements Serializable {
    private static final long serialVersionUID = 386724858837086698L;
    @ApiModelProperty(value = "部门Id")
    private String id;
    @ApiModelProperty(value = "部门名称")
    private String departmentName;
    @ApiModelProperty(value = "父部门Id")
    private String parentId;
    @TableField(exist = false)
    private List<FlowDepartment> children = new ArrayList<>();
    @ApiModelProperty(value = "部门负责人Id")
    private String managerId;
    @ApiModelProperty(value = "排序序号")
    private Integer sortIndex;
    @ApiModelProperty(value = "部门名称")
    private String departmentNames;

}
