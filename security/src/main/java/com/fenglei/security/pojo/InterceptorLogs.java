package com.fenglei.security.pojo;

import lombok.Data;

@Data
public class InterceptorLogs {

    private String id;

    private String method;

    private String requestUrl;

    private String queryString;

    private String creatorId;

    private String creator;

    private String createTimeStr;

    private String deptId;

    private String dept;

    private String customerId;

    private String customer;

    private String customerCateId;

    private String customerCate;
}
