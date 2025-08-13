package com.fenglei.mapper.inv;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.model.basedata.dto.MaterialDTO;
import com.fenglei.model.inv.entity.InvInventory;
import com.fenglei.model.inv.dto.InventoryDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InvInventoryMapper extends BaseMapper<InvInventory> {

    IPage<InvInventory> getPage(Page page, @Param("invInventory") InvInventory invInventory);

    List<InvInventory> getPage(@Param("invInventory") InvInventory invInventory);

    List<InvInventory> getList(@Param("invInventory") InvInventory invInventory);

    InvInventory infoById(String id);

    IPage<InvInventory> getPageByLot(Page page, InvInventory invInventory);

    IPage<InventoryDto> pageForPackage(Page<InventoryDto> page, @Param("inventoryDto") InventoryDto inventoryDto);


    List<MaterialDTO> getMaterialsByLots(@Param("lots") List<String> lots);
}
