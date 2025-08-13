package com.fenglei.model.system.vo;

import com.fenglei.model.system.entity.SysUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * app用户列表
 */
@Data
public class AppUserVo {

    @ApiModelProperty("姓的拼音首字母")
    private String firstPinYin;

    @ApiModelProperty("用户集合")
    private List<SysUser> users;
}
