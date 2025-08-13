package com.fenglei.model.system.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserDTO {

  private String id;
    private String username;
    private String mobile;
    private String password;
    private Integer status;
    private Integer count;
    private Integer cumulativeCount;
    private String registrationPhoneNumber;
    private String registrationPassword;
    private String wwUserid;
    private String wwAvatar;
    private String wwThumbAvatar;
    private List<String> roleIds;

    private String email;
    private Integer sys;
}
