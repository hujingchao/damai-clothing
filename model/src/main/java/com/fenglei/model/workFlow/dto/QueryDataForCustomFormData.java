package com.fenglei.model.workFlow.dto;

import lombok.Data;

import java.util.List;

/**
 * @author yzy
 */
@Data
public class QueryDataForCustomFormData {
    private String key;
    private String value;
    private List<QueryDataForCustomFormData> queryDataForCustomFormDataList;
    private String queryDataForCustomFormDataStr;
    private String companyId;
    private String userId;
}
