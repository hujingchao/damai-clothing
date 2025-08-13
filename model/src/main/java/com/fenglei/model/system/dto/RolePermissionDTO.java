package com.fenglei.model.system.dto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class RolePermissionDTO {
    private String roleId;
    private List<String> permissionIds;
    private Integer type;

    private String moduleId;
}
