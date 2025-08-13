package com.fenglei.model.base.pojo;

import lombok.Data;

/**
 * @author qj
 * @date 2021-03-10
 */
@Data
public class JWTPayload {

    private String jti;

    private Long exp;
}
