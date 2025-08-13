package com.fenglei.model.system.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class SysUserRole implements Serializable {

    private String userId;

    private String roleId;
}
