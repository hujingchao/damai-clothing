package com.fenglei.mapper.inv;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.model.inv.entity.InvPackage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenglei.model.inv.entity.InvPackageItem;
import com.fenglei.model.inv.vo.InvPackageItemVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 入库后打包 Mapper 接口
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-23
 */
public interface InvPackageMapper extends BaseMapper<InvPackage> {

    IPage<InvPackageItemVo> itemPage(Page page, @Param("package") InvPackage invPackage);
    List<InvPackageItemVo> itemPage(@Param("package") InvPackage invPackage);

}
