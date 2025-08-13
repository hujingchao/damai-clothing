package com.fenglei.service.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fenglei.common.constant.CertificationParams;
import com.fenglei.common.util.EncryptUtil;
import com.fenglei.common.util.HttpClientUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description 银联自助签约通用请求类
 * @Author zhouyiqiu
 * @Date 2021/12/30 11:09
 * @Version 1.0
 */
public class CommonReq<T,E> {

    public E commonReq(T data, Class<E> clazz) throws Exception {
        String dataStr = new ObjectMapper().writeValueAsString(data);
        String jsonData = EncryptUtil.encryptBy3DES(dataStr, CertificationParams.key);
        String signData = EncryptUtil.signBySHA256(dataStr);

        Map<String, String> params = new HashMap<>();
        params.put("json_data", jsonData);
        params.put("sign_data", signData);
        params.put("accesser_id", CertificationParams.accesserId);
        String resultStr = HttpClientUtil.doPost(CertificationParams.url, params);
        return new ObjectMapper().readValue(resultStr.getBytes(), clazz);
    }

    public E commonReqGet(T data, Class<E> clazz) throws Exception {
        String dataStr = new ObjectMapper().writeValueAsString(data);
        String jsonData = EncryptUtil.encryptBy3DES(dataStr, CertificationParams.key);
        String signData = EncryptUtil.signBySHA256(dataStr);

        Map<String, String> params = new HashMap<>();
        params.put("json_data", jsonData);
        params.put("sign_data", signData);
        params.put("accesser_id", CertificationParams.accesserId);
        String resultStr = HttpClientUtil.doGet(CertificationParams.url, params);
        return new ObjectMapper().readValue(resultStr.getBytes(), clazz);
    }
}
