package com.fenglei.mapper.inv;

import com.fenglei.model.inv.entity.InvPackageItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 入库后打包子表 Mapper 接口
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-23
 */
public interface InvPackageItemMapper extends BaseMapper<InvPackageItem> {

    List<InvPackageItem> listByPid(String pid);
}
