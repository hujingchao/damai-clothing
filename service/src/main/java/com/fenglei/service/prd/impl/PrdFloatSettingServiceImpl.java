package com.fenglei.service.prd.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fenglei.common.util.RequestUtils;
import com.fenglei.model.prd.entity.PrdFloatSetting;
import com.fenglei.mapper.prd.PrdFloatSettingMapper;
import com.fenglei.service.prd.IPrdFloatSettingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zgm
 * @since 2024-04-18
 */
@Service
public class PrdFloatSettingServiceImpl extends ServiceImpl<PrdFloatSettingMapper, PrdFloatSetting> implements IPrdFloatSettingService {

    /**
     * 新增
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PrdFloatSetting add(PrdFloatSetting prdFloatSetting) {
        save(prdFloatSetting);
        return prdFloatSetting;
    }

    /**
     * 删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean myRemoveById(String id) {
        return null;
    }

    /**
     * 批量删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean myRemoveByIds(List<String> ids) {
        return null;
    }

    /**
     * 更新
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean myUpdate(PrdFloatSetting prdFloatSetting) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        prdFloatSetting.setUpdateTime(sdf.format(new Date()));
        prdFloatSetting.setUpdaterId(RequestUtils.getUserId());
        return updateById(prdFloatSetting);
    }

    /**
     * 分页查询
     */
    @Override
    public IPage<PrdFloatSetting> myPage(Page page, PrdFloatSetting prdFloatSetting) {
        return null;
    }

    /**
     * 详情
     */
    @Override
    public PrdFloatSetting detail(String id) {
        return null;
    }

    @Override
    public PrdFloatSetting getSetting() {
        List<PrdFloatSetting> list = list(new LambdaQueryWrapper<PrdFloatSetting>()
                .orderByDesc(PrdFloatSetting::getCreateTime));
        return  list.get(0);
    }
}
