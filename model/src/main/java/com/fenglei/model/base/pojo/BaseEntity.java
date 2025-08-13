package com.fenglei.model.base.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 实体类的基类
 * @Author zhouyiqiu
 * @Date 2021/12/16 18:32
 * @Version 1.0
 */
@Data
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty("内码")
    private String id;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间")
    private String createTime;

    @TableField(exist = false)
    @ApiModelProperty("创建人")
    private String creator;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建人id")
    private String creatorId;

    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty("更新时间")
    private String updateTime;

    @TableField(exist = false)
    @ApiModelProperty("更新人")
    private String updater;

    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty("修改人id")
    private String updaterId;

    @TableField(exist = false)
    @ApiModelProperty("开始时间，用于过滤")
    private String beginDate;

    @TableField(exist = false)
    @ApiModelProperty("结束时间，用于过滤")
    private String endDate;

    @TableField(exist = false)
    private List<String> ids;

    @TableField(exist = false)
    @ApiModelProperty(value = "排除项ids")
    private List<String> excludeIds;

    @TableField(exist = false)
    @ApiModelProperty(value = "公共过滤")
    private String commFilter;

    @TableField(exist = false)
    private List<Integer> inStatus;
}
