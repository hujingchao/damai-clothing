package com.fenglei.service.basedata;

import com.fenglei.model.basedata.BdEquipment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;

/**
 * <p>
 * 设备 服务类
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-09
 */
public interface IBdEquipmentService extends IService<BdEquipment> {
    /**
     * 新增
     */
    BdEquipment add(BdEquipment bdEquipment);

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
    BdEquipment myUpdate(BdEquipment bdEquipment);

    /**
     * 分页查询
     */
    IPage<BdEquipment> myPage(Page page, BdEquipment bdEquipment);

    /**
     * 列表查询
     */
    List<BdEquipment> myList(BdEquipment bdEquipment);

    /**
     * 详情
     */
    BdEquipment detail(String id);
}
