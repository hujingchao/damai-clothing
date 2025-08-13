package com.fenglei.admin.controller.prd;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.result.Result;
import com.fenglei.model.prd.entity.PrdInStockItem;
import com.fenglei.service.prd.IPrdInStockItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 入库单分录 前端控制器
 * </p>
 *
 * @author zgm
 * @since 2024-04-18
 */
@Api(tags = "入库单分录 前端控制器")
@RestController
@RequestMapping("/api.admin/v1/prdInStockItem")
@AllArgsConstructor
@Slf4j
public class PrdInStockItemController {
    IPrdInStockItemService prdInStockItemService;

    @ApiOperation(value = "入库单分录新增")
    @PostMapping("/add")
    public Result<PrdInStockItem> add(@RequestBody PrdInStockItem prdInStockItem) {
        PrdInStockItem result = prdInStockItemService.add(prdInStockItem);
        return Result.success(result);
    }

    @ApiOperation(value = "入库单分录删除")
    @DeleteMapping("/removeById/{id}")
    public Result<Boolean> removeById(@PathVariable String id) {
        return Result.judge(prdInStockItemService.myRemoveById(id));
    }

    @ApiOperation(value = "入库单分录批量删除")
    @DeleteMapping("/removeByIds")
    public Result<Boolean> removeByIds(@RequestBody PrdInStockItem prdInStockItem) {
        return Result.judge(prdInStockItemService.myRemoveByIds(prdInStockItem.getIds()));
    }

    @ApiOperation(value = "入库单分录修改")
    @PutMapping("/update")
    public Result<Boolean> update(@RequestBody PrdInStockItem prdInStockItem) {
        return Result.judge(prdInStockItemService.myUpdate(prdInStockItem));
    }

    @ApiOperation(value = "入库单分录分页查询")
    @GetMapping("/page")
    public Result<IPage<PrdInStockItem>> page(Page page, PrdInStockItem prdInStockItem) {
        IPage<PrdInStockItem> result = prdInStockItemService.myPage(page, prdInStockItem);
        return Result.success(result);
    }

    @ApiOperation(value = "入库单分录详细信息")
    @GetMapping("/detail/{id}")
    public Result<PrdInStockItem> detail(@PathVariable String id) {
        PrdInStockItem result = prdInStockItemService.detail(id);
        return Result.success(result);
    }
}
