package com.fenglei.model.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fenglei.model.base.pojo.SimpleBaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class SysPermission extends SimpleBaseEntity {

    @TableId(type = IdType.AUTO)
    private String id;
    private String name;
    private String perm;
    private String moduleId;
    private String method;
    private Integer type;

    // 拥有资源权限角色ID集合
    @TableField(exist = false)
    private List<String> roleIds;
}
