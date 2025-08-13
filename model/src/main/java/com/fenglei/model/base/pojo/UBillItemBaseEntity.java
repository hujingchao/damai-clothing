package com.fenglei.model.base.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Data
public class UBillItemBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "内码")
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "主表id")
    private String pid;

    @ApiModelProperty(value = "序号")
    private Integer seq;

    @TableField(exist = false)
    @ApiModelProperty(value = "批量id")
    private List<String> ids;
    @TableField(exist = false)
    @ApiModelProperty(value = "批量主表id")
    private Collection<String> pids;
}
