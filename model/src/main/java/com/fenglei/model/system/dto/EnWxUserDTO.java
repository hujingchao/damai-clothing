package com.fenglei.model.system.dto;

import lombok.Data;

import java.util.List;

/**
 * 企业微信
 *
 * @author sxl
 */
@Data
public class EnWxUserDTO {

  private String id;
    private String username;
    private Integer status;
    private Integer count;
    private Integer cumulativeCount;
    private String registrationPhoneNumber;
    private String registrationPassword;
    private String wwUserid;
    private String wwAvatar;
    private String wwThumbAvatar;
    private String wwPassword;
    private List<String> roleIds;

    private String email;
    private Integer sys;
}
