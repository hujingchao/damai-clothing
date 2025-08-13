package com.fenglei.model.system.vo;

import lombok.Data;

import java.util.List;

@Data
public class UserVO {

    private Long id;

    private String username;

    private String nickname;

    private String mobile;

    private Integer gender;

    private String avatar;

    private String staffId;

    private String staffName;

    private String staffNo;

    private Integer count;

    private Integer cumulativeCount;

    private String registrationPhoneNumber;

    private String registrationPassword;

    private String wwUserid;

    private String wwAvatar;

    private String wwThumbAvatar;

    private List<String> roles;

    private String roleNames;

    private List<String> perms;

    private List<MenuVO> menus;

    private String email;
    private Integer sys;

    private List<MenuVO> parallelMenus;
}
