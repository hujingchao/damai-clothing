package com.fenglei.model.workFlow.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.BaseFlowPojo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author pc
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "RoleTableInfoItem", description = "角色表单列权限")
@Data
@TableName(value = "flow_Role_Table_Info_Item")
public class RoleTableInfoItem extends BaseFlowPojo {
    private static final long serialVersionUID = 6144906987661744100L;
    @ApiModelProperty(value = "角色ID")
    private String roleId;
    @ApiModelProperty(value = "表单子项Id")
    private String tableInfoItemId;
    @ApiModelProperty(value = "表单Id")
    private String tableInfoId;
    @ApiModelProperty(value = "表单子项列表")
    @TableField(exist = false)
    private List<TableInfoItem> tableInfoItems;
    @ApiModelProperty(value = "角色表单列权限列表")
    @TableField(exist = false)
    private List<RoleTableInfoItem> roleTableInfoItems;
}
