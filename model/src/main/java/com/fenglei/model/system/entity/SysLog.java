package com.fenglei.model.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 日志
 * </p>
 *
 * @author zhouyiqiu
 * @since 2022-05-05
 */
@Getter
@Setter
@TableName("sys_log")
@ApiModel(value = "SysLog对象", description = "日志")
public class SysLog implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 内码
     */
    @TableId(type = IdType.AUTO)
    private String id;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private String createTime;
    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String creator;
    /**
     * 创建人id
     */
    @TableField(fill = FieldFill.INSERT)
    private String creatorId;

    @ApiModelProperty("类名")
    private String className;

    @ApiModelProperty("方法名")
    private String methodName;

    @ApiModelProperty("参数")
    private String params;

    @ApiModelProperty("参数")
    private String paramsChinese;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("耗时")
    private long exeuTime;



    @TableField(exist = false)
    private String beginDate;
    @TableField(exist = false)
    private String endDate;
    @ApiModelProperty("id集合，用于接收前台传参，如批量删除")
    @TableField(exist = false)
    private List<String> ids;
}
