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
 * User: yzy
 * Date: 2019/9/24 0024
 * Time: 11:02
 * Description: No Description
 * @author yzy
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "TableInfo", description = "表单信息类")
@Data
@TableName(value = "sys_table_info")
public class TableInfo extends BaseFlowPojo {
    private static final long serialVersionUID = -2501798207137116979L;
    @ApiModelProperty(value = "表单名称")
    private String tableName;
    @ApiModelProperty(value = "表单编号")
    private String tableCode;
    @ApiModelProperty(value = "全名")
    private String fullName;
    @ApiModelProperty(value = "表单名称")
    private String formName;
    @TableField(exist = false)
    private List<TableInfoItem> tableInfoItems;
}
