package com.fenglei.mapper.workFlow;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.model.workFlow.dto.QueryDataForCustomFormData;
import com.fenglei.model.workFlow.entity.CustomFormData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yzy
 */

public interface CustomFormDataMapper extends BaseMapper<CustomFormData> {

    /**
     * 查询自定义表单
     * @param page 分页数据
     * @param queryDataForCustomFormDataList 获取自定义表单数据的查询数据
     * @param customFormId 自定义表单ID
     * @param userIds 用户Id集合
     * @return IPage<CustomFormData>
     */
    IPage<CustomFormData> selectCustomFormDataPage(Page<CustomFormData> page, @Param("queryDataForCustomFormDataList")List<QueryDataForCustomFormData> queryDataForCustomFormDataList, @Param("customFormId") String customFormId, @Param("userIds") List<String> userIds);
}
