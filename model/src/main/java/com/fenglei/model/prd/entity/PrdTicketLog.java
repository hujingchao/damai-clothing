package com.fenglei.model.prd.entity;

import java.util.List;

import com.baomidou.mybatisplus.annotation.*;
import com.fenglei.model.base.pojo.UBillBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhaojunnan
 * @since 2024-05-06
 */
@Getter
@Setter
@TableName("prd_ticket_log")
@ApiModel(value = "PrdTicketLog对象", description = "")
public class PrdTicketLog {

    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty("内码")
    private String id;

    @TableField(exist = false)
    @ApiModelProperty("创建人")
    private String creator;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间")
    private String createTime;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建人id")
    private String creatorId;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("更新时间")
    private String updateTime;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("修改人")
    private String updateId;

    @ApiModelProperty("事件描述")
    private String remark;

    @ApiModelProperty("id集合，用于接收前台传参，如批量删除")
    @TableField(exist = false)
    private List<String> ids;
}
