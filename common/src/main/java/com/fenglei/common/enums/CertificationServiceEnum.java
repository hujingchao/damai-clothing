package com.fenglei.common.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description 认证银联自助服务的服务类型枚举
 * @Author zhouyiqiu
 * @Date 2021/12/29 13:04
 * @Version 1.0
 */
public enum CertificationServiceEnum {
    PIC_UPLOAD("pic_upload"),
    COMPLEX_UPLOAD("complex_upload"),
    REQUEST_ACCOUNT_VERIFY("request_account_verify"),
    COMPANY_ACCOUNT_VERIFY("company_account_verify"),
    AGREEMENT_SIGN("agreement_sign"),
    APPLY_QRY("apply_qry"),
    DATA_DOWNLOAD("data_download"),
    BRANCH_BANK_LIST("branch_bank_list");

    @Getter
    @Setter
    private String code;

    CertificationServiceEnum(String code) {
        this.code=code;
    }

    public static CertificationServiceEnum getValue(String code){
        for (CertificationServiceEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return PIC_UPLOAD;
    }
}
