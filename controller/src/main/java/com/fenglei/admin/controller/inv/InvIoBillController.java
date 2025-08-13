package com.fenglei.admin.controller.inv;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.result.Result;
import com.fenglei.model.inv.entity.InvIoBill;
import com.fenglei.service.inv.IInvIoBillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 物料收发流水账 前端控制器
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-22
 */
@Api(tags = "物料收发流水账 前端控制器")
@RestController
@RequestMapping("/api.admin/v1/invIoBill")
@AllArgsConstructor
@Slf4j
public class InvIoBillController {
    IInvIoBillService invIoBillService;

    @ApiOperation(value = "物料收发流水账新增")
    @PostMapping("/add")
    public Result<InvIoBill> add(@RequestBody InvIoBill invIoBill) {
        InvIoBill result = invIoBillService.add(invIoBill);
        return Result.success(result);
    }

    @ApiOperation(value = "物料收发流水账删除")
    @DeleteMapping("/removeById/{id}")
    public Result<Boolean> removeById(@PathVariable String id) {
        return Result.judge(invIoBillService.myRemoveById(id));
    }

    @ApiOperation(value = "物料收发流水账批量删除")
    @DeleteMapping("/removeByIds")
    public Result<Boolean> removeByIds(@RequestBody InvIoBill invIoBill) {
        return Result.judge(invIoBillService.myRemoveByIds(invIoBill.getIds()));
    }

    @ApiOperation(value = "物料收发流水账修改")
    @PutMapping("/update")
    public Result<Boolean> update(@RequestBody InvIoBill invIoBill) {
        return Result.judge(invIoBillService.myUpdate(invIoBill));
    }

    @ApiOperation(value = "物料收发流水账分页查询")
    @PostMapping("/page")
    public Result<IPage<InvIoBill>> page(Page<InvIoBill> page, @RequestBody InvIoBill invIoBill) {
        IPage<InvIoBill> result = invIoBillService.myPage(page, invIoBill);
        return Result.success(result);
    }

    @ApiOperation(value = "物料收发流水账导出")
    @PostMapping("/exportInvIoBill")
    public void exportInvIoBill(HttpServletResponse response, @RequestBody InvIoBill invIoBill) {
        invIoBillService.exportInvIoBill(response, invIoBill);
    }

    @ApiOperation(value = "物料收发流水账详细信息")
    @GetMapping("/detail/{id}")
    public Result<InvIoBill> detail(@PathVariable String id) {
        InvIoBill result = invIoBillService.detail(id);
        return Result.success(result);
    }
}
