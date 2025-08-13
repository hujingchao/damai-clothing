package com.fenglei.service.prd;

import com.fenglei.model.prd.entity.PrdFloatSetting;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zgm
 * @since 2024-04-18
 */
public interface IPrdFloatSettingService extends IService<PrdFloatSetting> {
    /**
     * 新增
     */
    PrdFloatSetting add(PrdFloatSetting prdFloatSetting);

    /**
     * 删除
     */
    Boolean myRemoveById(String id);

    /**
     * 批量删除
     */
    Boolean myRemoveByIds(List<String> ids);

    /**
     * 更新
     */
    boolean myUpdate(PrdFloatSetting prdFloatSetting);

    /**
     * 分页查询
     */
    IPage<PrdFloatSetting> myPage(Page page, PrdFloatSetting prdFloatSetting);

    /**
     * 详情
     */
    PrdFloatSetting detail(String id);

    PrdFloatSetting getSetting();
}
