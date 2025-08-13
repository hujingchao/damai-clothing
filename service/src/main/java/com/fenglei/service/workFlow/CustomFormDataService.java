package com.fenglei.service.workFlow;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.model.workFlow.dto.QueryDataForCustomFormData;
import com.fenglei.model.workFlow.entity.CustomFormData;

/**
 * @author yzy
 */
public interface CustomFormDataService extends IService<CustomFormData> {

    /**
     * 获取自定义表单数据
     * @param page 分页信息
     * @param customFormData 查询信息
     * @param queryDataForCustomFormData 查询信息
     * @return IPage<CustomFormData>
     */
    IPage<CustomFormData> myPage(Page<CustomFormData> page, CustomFormData customFormData, QueryDataForCustomFormData queryDataForCustomFormData);

}
