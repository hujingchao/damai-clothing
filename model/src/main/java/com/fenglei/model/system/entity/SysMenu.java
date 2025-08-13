package com.fenglei.model.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fenglei.model.base.pojo.SimpleBaseEntity;
import lombok.Data;

import java.util.List;

/**
 * @author qj
 * @date 2020-11-06
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SysMenu extends SimpleBaseEntity {

    @TableId(type = IdType.AUTO)
    private String id;

    private String parentId;

    private String name;

    private String title;

    private String icon;

    private String path;

    private String component;

    private Integer sort;

    private Integer visible;

    private String redirect;

    private Boolean keepAlive;

    private Boolean isTitle;

    @TableField(exist = false)
    private List<Integer> roles;

    @TableField(exist = false)
    private List<?> children;



}
