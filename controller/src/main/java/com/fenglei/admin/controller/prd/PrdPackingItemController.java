package com.fenglei.admin.controller.prd;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.result.Result;
import com.fenglei.model.prd.entity.PrdPackingItem;
import com.fenglei.service.prd.IPrdPackingItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 包装单分录 前端控制器
 * </p>
 *
 * @author zgm
 * @since 2024-04-11
 */
@Api(tags = "包装单分录 前端控制器")
@RestController
@RequestMapping("/api.admin/v1/prdPackingItem")
@AllArgsConstructor
@Slf4j
public class PrdPackingItemController {
    IPrdPackingItemService prdPackingItemService;

    @ApiOperation(value = "包装单分录新增")
    @PostMapping("/add")
    public Result<PrdPackingItem> add(@RequestBody PrdPackingItem prdPackingItem) {
        PrdPackingItem result = prdPackingItemService.add(prdPackingItem);
        return Result.success(result);
    }

    @ApiOperation(value = "包装单分录删除")
    @DeleteMapping("/removeById/{id}")
    public Result<Boolean> removeById(@PathVariable String id) {
        return Result.judge(prdPackingItemService.myRemoveById(id));
    }

    @ApiOperation(value = "包装单分录批量删除")
    @DeleteMapping("/removeByIds")
    public Result<Boolean> removeByIds(@RequestBody PrdPackingItem prdPackingItem) {
        return Result.judge(prdPackingItemService.myRemoveByIds(prdPackingItem.getIds()));
    }

    @ApiOperation(value = "包装单分录修改")
    @PutMapping("/update")
    public Result<Boolean> update(@RequestBody PrdPackingItem prdPackingItem) {
        return Result.judge(prdPackingItemService.myUpdate(prdPackingItem));
    }

    @ApiOperation(value = "包装单分录分页查询")
    @GetMapping("/page")
    public Result<IPage<PrdPackingItem>> page(Page page, PrdPackingItem prdPackingItem) {
        IPage<PrdPackingItem> result = prdPackingItemService.myPage(page, prdPackingItem);
        return Result.success(result);
    }

    @ApiOperation(value = "包装单分录详细信息")
    @GetMapping("/detail/{id}")
    public Result<PrdPackingItem> detail(@PathVariable String id) {
        PrdPackingItem result = prdPackingItemService.detail(id);
        return Result.success(result);
    }
}
