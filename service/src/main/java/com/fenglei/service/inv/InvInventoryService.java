package com.fenglei.service.inv;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.model.basedata.dto.MaterialDTO;
import com.fenglei.model.inv.entity.InvInventory;
import com.fenglei.model.inv.entity.InvIoBill;
import com.fenglei.model.inv.dto.InventoryDto;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface InvInventoryService extends IService<InvInventory> {

    IPage<InvInventory> myPage(Page page, InvInventory invInventory);

    List<InvInventory> myList(InvInventory invInventory);

    InvInventory detail(String id);

    Boolean addQty(InvInventory invInventory, InvIoBill invIoBill, int ioType);

    Boolean batchAddQty(List<InvInventory> invInventorys, List<InvIoBill> invIoBills, int ioType);

    Boolean subQty(InvInventory invInventory, InvIoBill invIoBill, int ioType);

    Boolean batchSubQty(List<InvInventory> invInventorys, List<InvIoBill> invIoBills, int ioType);

    IPage getPageByLot(Page page, InvInventory invInventory);

    IPage<InventoryDto> pageForPackage(Page<InventoryDto> page, InventoryDto inventoryDto);

    List<MaterialDTO> getMaterialsByLots(List<String> lots);

    void exportInventory(HttpServletResponse response, InvInventory invInventory);
}
