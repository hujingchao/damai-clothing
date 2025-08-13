package com.fenglei.admin.controller.inv;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.result.Result;
import com.fenglei.model.inv.entity.InvInventory;
import com.fenglei.service.basedata.BdMaterialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ljw
 */
@Api(tags = "物料库存管理接口")
@RestController
@RequestMapping("/materialInv")
@RequiredArgsConstructor
public class MaterialInvController {

    @Resource
    private BdMaterialService materialService;

    @ApiOperation(value = "即时库存分页", notes = "即时库存分页")
    @GetMapping("/page")
    public Result<IPage<InvInventory>> page(Page<InvInventory> page, InvInventory invInventory) throws Exception {
        IPage<InvInventory> iPage = materialService.pageForInv(page, invInventory);
        return Result.success(iPage);
    }

    @ApiOperation(value = "即时库存导出")
    @GetMapping("/exportInventory")
    public void exportInventory(HttpServletResponse response, InvInventory invInventory) throws Exception {
        materialService.exportMaterialInventory(response, invInventory);
    }
}
