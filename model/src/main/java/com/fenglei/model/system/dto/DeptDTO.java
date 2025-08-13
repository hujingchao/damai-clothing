package com.fenglei.model.system.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zjn
 * \* Date: 2021/6/11
 * \* Time: 10:20
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@Data
@Accessors(chain = true)
public class DeptDTO {
  private String id;

    private String name;

    private Long parentId;

    private String treePath;

    private Integer sort;

    private Integer status;

    private Integer deleted;

    private String leader;

    private String mobile;

    private String email;

    private String managerId;
}
