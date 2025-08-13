package com.fenglei.service.workFlow.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.mapper.workFlow.CustomFormDataMapper;
import com.fenglei.model.workFlow.dto.QueryDataForCustomFormData;
import com.fenglei.model.workFlow.entity.CustomFormData;
import com.fenglei.service.workFlow.CustomFormDataService;
import com.fenglei.service.workFlow.SystemInformationAcquisitionService;
import com.fenglei.service.workFlow.util.ClassConversionTools;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author yzy
 */
@Service
public class CustomFormDataServiceImpl extends ServiceImpl<CustomFormDataMapper, CustomFormData> implements CustomFormDataService {

    @Resource
    private SystemInformationAcquisitionService systemInformationAcquisitionService;

    @Override
    public IPage<CustomFormData> myPage(Page<CustomFormData> page, CustomFormData customFormData, QueryDataForCustomFormData queryDataForCustomFormData){
        List<String> userIds = null;
        if (systemInformationAcquisitionService.getCheckBySuperior(queryDataForCustomFormData.getCompanyId())) {
            userIds = systemInformationAcquisitionService.getDataReadableUserIds(queryDataForCustomFormData.getUserId(), queryDataForCustomFormData.getCompanyId());
        }
        String jsonStr = queryDataForCustomFormData.getQueryDataForCustomFormDataStr();
        Map<String, Object> stringToMap = JSONObject.parseObject(jsonStr);
        List<String> keys = ClassConversionTools.castList(new ArrayList<>(stringToMap.keySet()), String.class);
        List<QueryDataForCustomFormData> queryDataForCustomFormDataList = new ArrayList<>();
        for (String key : keys) {
            if (Objects.nonNull(stringToMap.get(key))) {
                QueryDataForCustomFormData queryDataForCustomFormData1 = new QueryDataForCustomFormData();
                queryDataForCustomFormData1.setKey(key);
                queryDataForCustomFormData1.setValue(String.valueOf(stringToMap.get(key)));
                queryDataForCustomFormDataList.add(queryDataForCustomFormData1);
            }
        }
        List<QueryDataForCustomFormData> queryDataForCustomFormDataList1 = (queryDataForCustomFormDataList.size() == 0 ? null : queryDataForCustomFormDataList);
        return baseMapper.selectCustomFormDataPage(page, queryDataForCustomFormDataList1, customFormData.getCustomFormId(), userIds);
    }
}
