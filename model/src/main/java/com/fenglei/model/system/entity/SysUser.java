package com.fenglei.model.system.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description 用户类
 * @Author zhouyiqiu
 * @Date 2021/12/2 13:51
 * @Version 1.0
 */
@Data
public class SysUser implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private String id;

    private String username;

    private String nickname;

    private String mobile;

    private Integer gender;

    private String avatar;

    private String password;

    private Integer status;

    private String deptId;
    /**
     * 手机
     */
    private String phone;

    //    @ApiModelProperty("逻辑删除标识 0-未删除 1-已删除")
//    @TableLogic(value = "0", delval = "1")
    private Integer deleted;

//    @TableField(exist = false)
//    private String deptName;

    @TableField(exist = false)
    private List<String> roleIds;
    @TableField(exist = false)
    private List<String> roles;

    @TableField(exist = false)
    private String roleNames;

    @ApiModelProperty(value = "是否是审核管理员")
    private Boolean isAuditManager;
    /**
     * 天眼查
     */
    @ApiModelProperty(value = "接口调用次数")
    private Integer count;

    @ApiModelProperty(value = "累计调用次数")
    private Integer cumulativeCount;

    @ApiModelProperty(value = "注册电话号码")
    private String registrationPhoneNumber;

    @ApiModelProperty(value = "注册电话的密码")
    private String registrationPassword;

    @ApiModelProperty(value = "企业微信userid")
    private String wwUserid;

    @ApiModelProperty(value = "企业微信userid加密")
    private String wwPassword;

    @ApiModelProperty(value = "微信头像url")
    private String wwAvatar;

    @ApiModelProperty(value = "微信头像头像缩略图url")
    private String wwThumbAvatar;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 系统保留  0：否   1：是
     */
    private Integer sys;

    @TableField(fill = FieldFill.INSERT)
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtModified;

    @TableField(exist = false)
    private List<String> excludeIds;

    /**
     * 姓名或者手机号
     */
    @TableField(exist = false)
    private String nameOrPhone;

    /**
     * 旧密码，用于修改密码
     */
    @TableField(exist = false)
    private String oldPwd;
}
