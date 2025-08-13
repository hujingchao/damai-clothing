package com.fenglei.security.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

@Data
public class CustomerContactUserDetail implements UserDetails {

    private String nickname;

    private static final long serialVersionUID = 2700836460128565512L;

    private String id;

    private Date createTime = new Date();

    private Date updateTime = new Date();

    @ApiModelProperty("keyNo")
    private String keyNo;

    @ApiModelProperty("联系人名称")
    private String contactName;

    @ApiModelProperty("性别")
    private String sex;

    private Boolean enabled;

    @ApiModelProperty("电话号码")
    private String phoneNumber;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("qq")
    private String qq;
    @ApiModelProperty("微信")
    private String wechat;

    @ApiModelProperty("客户ID")
    private String customerId;

    @ApiModelProperty("地址")
    private String address;

    @TableField(exist = false)
    private String customerNo;

    @TableField(exist = false)
    private String customerName;

    @ApiModelProperty("外部联系人的userid")
    private String externalUserId;

    @ApiModelProperty("外部联系人的头像")
    private String wwAvatar;

    @TableField(exist = false)
    private String customer;

    @ApiModelProperty("统一社会信用代码")
    @TableField(exist = false)
    private String creditCode;

    @ApiModelProperty("企业微信用户标识（不是为null）")
    private String wwType;

    @ApiModelProperty("归属企业微信 crm系统userId")
    private String wwOpenId;

    @ApiModelProperty("企业微信客户备注")
    private String wwRemark;

    @ApiModelProperty("客户来源（1、系统内添加 2、微信（企业）")
    private Integer type = 1;

    @ApiModelProperty("职位")
    private String position;

    @TableField(exist = false)
    private String[] ids;

    @ApiModelProperty(value = "标签")
    private String labelTags;

    @ApiModelProperty(value = "负责人Id")
    private String salesmanId;

    @ApiModelProperty("企业微信openId")
    private String wxOpenId;


    @ApiModelProperty("关键决策人，是为1否为0")
    private String keypeople;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty("创建人")
    private String createUser;

    @ApiModelProperty("最后修改人")
    private String updateUser;

    @ApiModelProperty("最后跟进时间")
    private String followDate;

    @TableField(exist = false)
    private String salesMan;

    @ApiModelProperty("小程序openId")
    private String mpOpenid;

    @ApiModelProperty("小程序用户名")
    private String mpUsername;

    @ApiModelProperty("小程序密码")
    private String mpPassword;

    @ApiModelProperty("小程序头像")
    private String mpAvatar;

    private Collection<SimpleGrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.mpPassword;
    }

    @Override
    public String getUsername() {
        return this.phoneNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
