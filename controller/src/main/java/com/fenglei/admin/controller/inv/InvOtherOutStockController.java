package com.fenglei.admin.controller.inv;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.result.Result;
import com.fenglei.model.inv.entity.InvOtherOutStock;
import com.fenglei.service.inv.IInvOtherOutStockService;
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
@RequestMapping("/api.admin/v1/otherOutStock")
@AllArgsConstructor
@Slf4j
public class InvOtherOutStockController {
    IInvOtherOutStockService invOtherOutStockService;

    @ApiOperation(value = "新增")
    @PostMapping("/add")
    public Result<InvOtherOutStock> add(@RequestBody InvOtherOutStock invOtherOutStock) {
        InvOtherOutStock result = invOtherOutStockService.add(invOtherOutStock);
        return Result.success(result);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("/removeById/{id}")
    public Result<Boolean> removeById(@PathVariable String id) {
        return Result.judge(invOtherOutStockService.myRemoveById(id));
    }

    @ApiOperation(value = "批量删除")
    @DeleteMapping("/removeByIds")
    public Result<Boolean> removeByIds(@RequestBody InvOtherOutStock invOtherOutStock) {
        return Result.judge(invOtherOutStockService.myRemoveByIds(invOtherOutStock.getIds()));
    }

    @ApiOperation(value = "修改")
    @PutMapping("/update")
    public Result<Boolean> update(@RequestBody InvOtherOutStock invOtherOutStock) {
        return Result.judge(invOtherOutStockService.myUpdate(invOtherOutStock));
    }

    @ApiOperation(value = "分页查询")
    @GetMapping("/page")
    public Result<IPage<InvOtherOutStock>> page(Page page, InvOtherOutStock invOtherOutStock) {
        IPage<InvOtherOutStock> result = invOtherOutStockService.myPage(page, invOtherOutStock);
        return Result.success(result);
    }

    @ApiOperation(value = "详细信息")
    @GetMapping("/detail/{id}")
    public Result<InvOtherOutStock> detail(@PathVariable String id) {
        InvOtherOutStock result = invOtherOutStockService.detail(id);
        return Result.success(result);
    }


    @PutMapping("/submit/{id}")
    public Result submit(@PathVariable String id) throws Exception {
        InvOtherOutStock result = invOtherOutStockService.submit(id);
        return Result.success(result);
    }

    @PutMapping(value = "/batchSubmitByIds")
    public Result batchSubmitByIds(@RequestBody String[] ids) throws Exception {
        String result = invOtherOutStockService.batchSubmitByIds(ids);
        return Result.success(result);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "otherOutStock", paramType = "body", dataType = "InvOtherOutStock"),
    })
    @ApiOperation(value = "执行操作 提交/审核/反审核")
    @PostMapping("/doAction")
    public Result doAction(@RequestBody InvOtherOutStock otherOutStock) throws Exception {
        return invOtherOutStockService.doAction(otherOutStock);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "otherOutStock", paramType = "body", dataType = "InvOtherOutStock"),
    })
    @ApiOperation(value = "批量执行操作 提交/审核/反审核")
    @PostMapping("/batchDoAction")
    public Result batchDoAction(@RequestBody InvOtherOutStock otherOutStock) throws Exception {
        return invOtherOutStockService.batchDoAction(otherOutStock);
    }

    @PutMapping("/unAudit/{id}")
    public Result unAudit(@PathVariable String id) throws Exception {
        InvOtherOutStock result = invOtherOutStockService.unAudit(id);
        return Result.success(result);
    }

    @PutMapping(value = "/batchUnAuditByIds")
    public Result batchUnAuditByIds(@RequestBody String[] ids) throws Exception {
        String result = invOtherOutStockService.batchUnAuditByIds(ids);
        return Result.success(result);
    }

}
