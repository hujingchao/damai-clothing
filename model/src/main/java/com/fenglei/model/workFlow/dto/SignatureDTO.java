package com.fenglei.model.workFlow.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zjn
 * \* Date: 2021/6/8
 * \* Time: 15:06
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@Data
public class SignatureDTO implements Serializable {
    private String imgStr;
    private String imgUploadUrl;
    private String imgUploadFolder;
}
