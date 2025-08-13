package com.fenglei.admin.controller.prd;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.annotation.SysLogAnnotation;
import com.fenglei.common.result.Result;
import com.fenglei.model.prd.entity.PrdInStock;
import com.fenglei.model.prd.entity.PrdInStockItem;
import com.fenglei.model.prd.vo.PrdInStockItemVo;
import com.fenglei.service.prd.IPrdInStockService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 入库单 前端控制器
 * </p>
 *
 * @author zgm
 * @since 2024-04-18
 */
@Api(tags = "入库单 前端控制器")
@RestController
@RequestMapping("/api.admin/v1/prdInStock")
@AllArgsConstructor
@Slf4j
public class PrdInStockController {
    IPrdInStockService prdInStockService;

    @ApiOperation(value = "入库单新增")
    @PostMapping("/add")
    @SysLogAnnotation("收货")
    public Result<PrdInStock> add(@RequestBody PrdInStock prdInStock) {
        PrdInStock result = prdInStockService.add(prdInStock);
        return Result.success(result);
    }

    @ApiOperation(value = "入库单删除")
    @DeleteMapping("/removeById/{id}")
    public Result<Boolean> removeById(@PathVariable String id) {
        return Result.judge(prdInStockService.myRemoveById(id));
    }





    @ApiOperation(value = "入库单分页查询")
    @GetMapping("/page")
    public Result<IPage<PrdInStock>> page(Page page, PrdInStock prdInStock) {
        IPage<PrdInStock> result = prdInStockService.myPage(page, prdInStock);
        return Result.success(result);
    }

    @ApiOperation(value = "包装单分页查询")
    @GetMapping("/wxPage")
    public Result<IPage<PrdInStockItemVo>> wxPage(Page page, PrdInStockItemVo itemVo) {
        IPage<PrdInStockItemVo> result = prdInStockService.wxPage(page, itemVo);
        return Result.success(result);
    }

    @ApiOperation(value = "包装单分页查询")
    @GetMapping("/getStatusCountAll")
    public Result<List<Integer>> getStatusCountAll(PrdInStockItemVo itemVo) {
        List<Integer> result = prdInStockService.getStatusCountAll(itemVo);
        return Result.success(result);
    }

    @ApiOperation(value = "包装单分录详细信息")
    @GetMapping("/itemDetail/{id}/{itemId}")
    public Result<PrdInStock> itemDetail(@PathVariable("id") String id, @PathVariable("itemId") String itemId) {
        PrdInStock result = prdInStockService.itemDetail(id,itemId);
        return Result.success(result);
    }

    @ApiOperation(value = "包装单分录是否置顶")
    @GetMapping("/itemSetTop/{id}/{setTop}")
    public Result<Boolean> itemSetTop(@PathVariable String id,@PathVariable Integer setTop ) {
        return Result.judge(prdInStockService.itemSetTop(id,setTop));
    }

    @ApiOperation(value = "包装单分录删除")
    @DeleteMapping("/itemRemove/{id}")
    public Result<Boolean> itemRemove(@PathVariable String id) {
        return Result.judge(prdInStockService.itemRemove(id));
    }

    @ApiOperation(value = "包装单修改")
    @PostMapping("/itemUpdate")
    public Result<Boolean> itemUpdate(@RequestBody PrdInStock prdInStock) {
        return Result.judge(prdInStockService.itemUpdate(prdInStock));
    }

    @ApiOperation(value = "校验数量")
    @PostMapping("/checkInStockCount")
    public Result<Boolean> checkInStockCount(@RequestBody List<PrdInStockItem> itemList) {
        return Result.judge(prdInStockService.checkInStockCount(itemList));
    }
}
