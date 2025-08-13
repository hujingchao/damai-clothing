package com.fenglei.model.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("sys_wechat_auth")
public class SysWechatAuth {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("openid")
    private String openid;

    @ApiModelProperty("sessionKey")
    private String sessionKey;

    @ApiModelProperty("性别")
    private Integer gender;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("市")
    private String city;

    @ApiModelProperty("国家")
    private String country;

    @ApiModelProperty("语言")
    private String language;

    @ApiModelProperty("省")
    private String province;

}
