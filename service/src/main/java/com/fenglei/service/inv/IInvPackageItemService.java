package com.fenglei.service.inv;

import com.fenglei.model.inv.entity.InvPackageItem;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * <p>
 * 入库后打包子表 服务类
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-23
 */
public interface IInvPackageItemService extends IService<InvPackageItem> {

    /**
     * 批量新增
     */
    List<InvPackageItem> batchAdd(List<InvPackageItem> packageItems);

    Boolean removeByPid(String pid);

    List<InvPackageItem> listByPid(String pid);
}
