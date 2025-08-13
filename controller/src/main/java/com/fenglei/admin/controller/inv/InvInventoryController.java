package com.fenglei.admin.controller.inv;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.result.Result;
import com.fenglei.common.util.StringUtils;
import com.fenglei.model.basedata.dto.MaterialDTO;
import com.fenglei.model.fin.dto.PieceRateFilter;
import com.fenglei.model.inv.entity.InvInventory;
import com.fenglei.model.inv.dto.InventoryDto;
import com.fenglei.model.inv.dto.LotDTO;
import com.fenglei.service.inv.InvInventoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ljw
 */
@Api(tags = "即时库存管理接口")
@RestController
@RequestMapping("/InvInventory")
@RequiredArgsConstructor
public class InvInventoryController {

    @Resource
    private InvInventoryService invInventoryService;

    @ApiOperation(value = "即时库存分页", notes = "即时库存分页")
    @GetMapping("/page")
    public Result page(Page page, InvInventory invInventory) throws Exception {
        IPage iPage = invInventoryService.myPage(page, invInventory);
        return Result.success(iPage);
    }

    @ApiOperation(value = "app即时库存分页", notes = "app即时库存分页")
    @PostMapping("/appPage")
    public Result appPage(Page page,@RequestBody InvInventory invInventory) throws Exception {
        IPage iPage = invInventoryService.myPage(page, invInventory);
        return Result.success(iPage);
    }

    @ApiOperation(value = "即时库存列表", notes = "即时库存列表")
    @GetMapping("/list")
    public Result list(InvInventory invInventory) throws Exception {
        List<InvInventory> list = invInventoryService.myList(invInventory);
        return Result.success(list);
    }

    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable String id) throws Exception {
        InvInventory result = invInventoryService.detail(id);
        return Result.success(result);
    }

    @ApiOperation(value = "即时库存按包号分页", notes = "即时库存按包号分页")
    @GetMapping("/getPageByLot")
    public Result getPageByLot(Page page, InvInventory invInventory) throws Exception {
        IPage iPage = invInventoryService.getPageByLot(page, invInventory);
        return Result.success(iPage);
    }

    @ApiOperation(value = "获取未打包的成品库存", notes = "获取未打包的成品库存")
    @PostMapping("/pageForPackage")
    public Result<IPage<InventoryDto>> pageForPackage(Page<InventoryDto> page, @RequestBody InventoryDto inventoryDto) throws Exception {
        IPage<InventoryDto> iPage = invInventoryService.pageForPackage(page, inventoryDto);
        return Result.success(iPage);
    }


    @ApiOperation(value = "查询所有批号", notes = "查询所有批号")
    @GetMapping("/getAllLot")
    public Result getAllLot(InvInventory invInventory) throws Exception {
        List<InvInventory> invInventoryList = invInventoryService.list(new LambdaQueryWrapper<InvInventory>()
                .isNotNull(InvInventory::getLot)
                .ne(InvInventory::getLot,"")
                .gt(InvInventory::getQty,0)
        );
        List<LotDTO> lotDTOList = new ArrayList<>();
        if(invInventoryList.size()>0){
            Set<String> lots = invInventoryList.stream().map(InvInventory::getLot).collect(Collectors.toSet());
            for (String lot : lots) {
                LotDTO lotDTO = new LotDTO();
                lotDTO.setValue(lot);
                lotDTO.setLabel(lot);
                lotDTOList.add(lotDTO);
            }
        }
        return Result.success(lotDTOList);
    }


    @ApiOperation(value = "按包号批量查询产品库存", notes = "按包号批量查询产品库存")
    @PostMapping("/getMaterialsByLots")
    public Result getMaterialsByLots(@RequestBody List<String> lots) throws Exception {
        List<MaterialDTO> materialDTOS = invInventoryService.getMaterialsByLots(lots);
        return Result.success(materialDTOS);
    }

    @ApiOperation(value = "即时库存导出")
    @GetMapping("/exportInventory")
    public void exportInventory(HttpServletResponse response, InvInventory invInventory) throws Exception {
        invInventoryService.exportInventory(response, invInventory);
    }
}
