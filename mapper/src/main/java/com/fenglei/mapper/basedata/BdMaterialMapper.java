package com.fenglei.mapper.basedata;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.model.basedata.BdMaterial;
import com.fenglei.model.inv.entity.InvInventory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BdMaterialMapper extends BaseMapper<BdMaterial> {

    IPage<BdMaterial> getPage(Page page, @Param("bdMaterial") BdMaterial bdMaterial);

    IPage<BdMaterial> getPageWithTemp(Page page, @Param("bdMaterial") BdMaterial bdMaterial);

    List<BdMaterial> getList(@Param("bdMaterial") BdMaterial bdMaterial);

    BdMaterial infoById(String id);

    List<BdMaterial> listMtrlNum(@Param("bdMaterial") BdMaterial bdMaterial);

    List<String> getNos(String noLike);

    IPage<BdMaterial> getPageFin(Page page, @Param("bdMaterial") BdMaterial bdMaterial);

    List<BdMaterial> getListFin(@Param("bdMaterial") BdMaterial bdMaterial);

    IPage<InvInventory> pageForInv(Page<InvInventory> page, @Param("invInventory") InvInventory invInventory);

    List<InvInventory> pageForInv(@Param("invInventory") InvInventory invInventory);
}
