package com.fenglei.model.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fenglei.model.base.pojo.SimpleBaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SysDictItem extends SimpleBaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String value;

    private String dictCode;

    private String sort;

    private Integer status;

    private Integer defaulted;

    private String remark;
    /**
     * 公用临时参数，勿删
     */
    @TableField(exist = false)
    private String param;
}
