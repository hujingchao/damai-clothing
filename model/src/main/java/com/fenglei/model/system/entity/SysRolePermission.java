package com.fenglei.model.system.entity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class SysRolePermission implements Serializable {
    private static final long serialVersionUID = 1L;
    private String roleId;
    private String permissionId;
}
