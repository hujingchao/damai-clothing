package com.fenglei.model.system.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author zhouyiqiu
 * @Date 2021/11/8 14:13
 * @Version 1.0
 * @Description
 */
@Data
public class AuthorizeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<AuthorizeItemDTO> orgAuths;
}
