package com.fenglei.admin.controller.inv;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.result.Result;
import com.fenglei.model.inv.entity.InvOtherInStock;
import com.fenglei.service.inv.IInvOtherInStockService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zgm
 * @since 2024-04-28
 */
@Api(tags = " 前端控制器")
@RestController
@RequestMapping("/api.admin/v1/otherInStock")
@AllArgsConstructor
@Slf4j
public class InvOtherInStockController {
    IInvOtherInStockService invOtherInStockService;

    @ApiOperation(value = "新增")
    @PostMapping("/add")
    public Result<InvOtherInStock> add(@RequestBody InvOtherInStock invOtherInStock) {
        InvOtherInStock result = invOtherInStockService.add(invOtherInStock);
        return Result.success(result);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("/removeById/{id}")
    public Result<Boolean> removeById(@PathVariable String id) {
        return Result.judge(invOtherInStockService.myRemoveById(id));
    }

    @ApiOperation(value = "批量删除")
    @DeleteMapping("/removeByIds")
    public Result<Boolean> removeByIds(@RequestBody InvOtherInStock invOtherInStock) {
        return Result.judge(invOtherInStockService.myRemoveByIds(invOtherInStock.getIds()));
    }

    @ApiOperation(value = "修改")
    @PutMapping("/update")
    public Result<Boolean> update(@RequestBody InvOtherInStock invOtherInStock) {
        return Result.judge(invOtherInStockService.myUpdate(invOtherInStock));
    }

    @ApiOperation(value = "分页查询")
    @GetMapping("/page")
    public Result<IPage<InvOtherInStock>> page(Page page, InvOtherInStock invOtherInStock) {
        IPage<InvOtherInStock> result = invOtherInStockService.myPage(page, invOtherInStock);
        return Result.success(result);
    }

    @ApiOperation(value = "详细信息")
    @GetMapping("/detail/{id}")
    public Result<InvOtherInStock> detail(@PathVariable String id) {
        InvOtherInStock result = invOtherInStockService.detail(id);
        return Result.success(result);
    }


    @PutMapping("/submit/{id}")
    public Result submit(@PathVariable String id) throws Exception {
        InvOtherInStock result = invOtherInStockService.submit(id);
        return Result.success(result);
    }

    @PutMapping(value = "/batchSubmitByIds")
    public Result batchSubmitByIds(@RequestBody String[] ids) throws Exception {
        String result = invOtherInStockService.batchSubmitByIds(ids);
        return Result.success(result);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "otherInStock", paramType = "body", dataType = "InvOtherInStock"),
    })
    @ApiOperation(value = "执行操作 提交/审核/反审核")
    @PostMapping("/doAction")
    public Result doAction(@RequestBody InvOtherInStock otherInStock) throws Exception {
        return invOtherInStockService.doAction(otherInStock);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "otherInStock", paramType = "body", dataType = "InvOtherInStock"),
    })
    @ApiOperation(value = "批量执行操作 提交/审核/反审核")
    @PostMapping("/batchDoAction")
    public Result batchDoAction(@RequestBody InvOtherInStock otherInStock) throws Exception {
        return invOtherInStockService.batchDoAction(otherInStock);
    }

    @PutMapping("/unAudit/{id}")
    public Result unAudit(@PathVariable String id) throws Exception {
        InvOtherInStock result = invOtherInStockService.unAudit(id);
        return Result.success(result);
    }

    @PutMapping(value = "/batchUnAuditByIds")
    public Result batchUnAuditByIds(@RequestBody String[] ids) throws Exception {
        String result = invOtherInStockService.batchUnAuditByIds(ids);
        return Result.success(result);
    }

}
