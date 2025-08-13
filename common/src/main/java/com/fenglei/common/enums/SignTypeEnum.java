package com.fenglei.common.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description 签名方式
 * @Author zhouyiqiu
 * @Date 2021/12/29 13:22
 * @Version 1.0
 */
public enum SignTypeEnum {
    SHA256("SHA-256");

    @Getter
    @Setter
    private String code;

    SignTypeEnum(String code) {
        this.code=code;
    }

    public static SignTypeEnum getValue(String code){
        for (SignTypeEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return SHA256;
    }
}
