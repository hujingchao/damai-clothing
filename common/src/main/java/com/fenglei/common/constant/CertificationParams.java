package com.fenglei.common.constant;

/**
 * @Description 认证银联自助服务参数
 * @Author zhouyiqiu
 * @Date 2021/12/29 9:58
 * @Version 1.0
 */
public interface CertificationParams {
    /**
     * 接口调用地址
     */
    String url = "https://selfapply-test.chinaums.com/self-contract-nmrs/interface/autoReg";


//    String url = "https://yinshangpai.chinaums.com/self-contract-nmrs/interface/autoReg";

    /**
     * 3DES加密密钥
     */
    String key = "udik876ehjde32dU61edsxsf";

    /**
     * 接入平台id
     */
    String accesserId = "898150173726006";

    /**
     * 成功返回码
     */
    String successCode = "0000";
}
