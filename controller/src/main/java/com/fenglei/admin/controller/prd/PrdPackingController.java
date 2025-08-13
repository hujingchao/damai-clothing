package com.fenglei.admin.controller.prd;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.result.Result;
import com.fenglei.model.prd.entity.PrdPacking;
import com.fenglei.model.prd.vo.PrdPackingItemVo;
import com.fenglei.service.prd.IPrdPackingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 包装单 前端控制器
 * </p>
 *
 * @author zgm
 * @since 2024-04-11
 */
@Api(tags = "包装单 前端控制器")
@RestController
@RequestMapping("/api.admin/v1/prdPacking")
@AllArgsConstructor
@Slf4j
public class PrdPackingController {
    IPrdPackingService prdPackingService;

    @ApiOperation(value = "包装单新增")
    @PostMapping("/add")
    public Result<PrdPacking> add(@RequestBody PrdPacking prdPacking) {
        PrdPacking result = prdPackingService.add(prdPacking);
        return Result.success(result);
    }

    @ApiOperation(value = "包装单删除")
    @DeleteMapping("/removeById/{id}")
    public Result<Boolean> removeById(@PathVariable String id) {
        return Result.judge(prdPackingService.myRemoveById(id));
    }

    @ApiOperation(value = "包装单批量删除")
    @DeleteMapping("/removeByIds")
    public Result<Boolean> removeByIds(@RequestBody PrdPacking prdPacking) {
        return Result.judge(prdPackingService.myRemoveByIds(prdPacking.getIds()));
    }




    @ApiOperation(value = "包装单分页查询")
    @GetMapping("/page")
    public Result<IPage<PrdPacking>> page(Page page, PrdPacking prdPacking) {
        IPage<PrdPacking> result = prdPackingService.myPage(page, prdPacking);
        return Result.success(result);
    }






    @ApiOperation(value = "包装单分页查询")
    @GetMapping("/wxPage")
    public Result<IPage<PrdPackingItemVo>> wxPage(Page page, PrdPackingItemVo itemVo) {
        IPage<PrdPackingItemVo> result = prdPackingService.wxPage(page, itemVo);
        return Result.success(result);
    }

    @ApiOperation(value = "包装单分页查询")
    @GetMapping("/getStatusCountAll")
    public Result<List<Integer>> getStatusCountAll(PrdPackingItemVo itemVo) {
        List<Integer> result = prdPackingService.getStatusCountAll(itemVo);
        return Result.success(result);
    }

    @ApiOperation(value = "包装单分录详细信息")
    @GetMapping("/itemDetail/{id}/{itemId}")
    public Result<PrdPacking> itemDetail(@PathVariable("id") String id, @PathVariable("itemId") String itemId) {
        PrdPacking result = prdPackingService.itemDetail(id,itemId);
        return Result.success(result);
    }

    @ApiOperation(value = "包装单分录是否置顶")
    @GetMapping("/itemSetTop/{id}/{setTop}")
    public Result<Boolean> itemSetTop(@PathVariable String id,@PathVariable Integer setTop ) {
        return Result.judge(prdPackingService.itemSetTop(id,setTop));
    }

    @ApiOperation(value = "包装单分录删除")
    @DeleteMapping("/itemRemove/{id}")
    public Result<Boolean> itemRemove(@PathVariable String id) {
        return Result.judge(prdPackingService.itemRemove(id));
    }

    @ApiOperation(value = "包装单修改")
    @PostMapping("/itemUpdate")
    public Result<Boolean> itemUpdate(@RequestBody PrdPacking prdPacking) {
        return Result.judge(prdPackingService.itemUpdate(prdPacking));
    }
}
