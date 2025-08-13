package com.fenglei.admin.controller.inv;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.result.Result;
import com.fenglei.model.inv.entity.InvPackage;
import com.fenglei.model.inv.entity.InvPackageItem;
import com.fenglei.model.inv.entity.InvPackageNo;
import com.fenglei.model.inv.vo.InvPackageItemVo;
import com.fenglei.service.inv.IInvPackageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 入库后打包 前端控制器
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-23
 */
@Api(tags = "入库后打包 前端控制器")
@RestController
@RequestMapping("/api.admin/v1/invPackage")
@AllArgsConstructor
@Slf4j
public class InvPackageController {
    IInvPackageService invPackageService;

    @ApiOperation(value = "入库后打包新增")
    @PostMapping("/add")
    public Result<InvPackage> add(@RequestBody InvPackage invPackage) {
        InvPackage result = invPackageService.add(invPackage);
        return Result.success(result);
    }

    @ApiOperation(value = "入库后打包删除")
    @DeleteMapping("/removeById/{id}")
    public Result<Boolean> removeById(@PathVariable String id) {
        return Result.judge(invPackageService.myRemoveById(id));
    }

    @ApiOperation(value = "入库后打包批量删除")
    @DeleteMapping("/removeByIds")
    public Result<Boolean> removeByIds(@RequestBody InvPackage invPackage) {
        return Result.judge(invPackageService.myRemoveByIds(invPackage.getIds()));
    }

    @ApiOperation(value = "入库后打包修改")
    @PutMapping("/update")
    public Result<Boolean> update(@RequestBody InvPackage invPackage) {
        return Result.judge(invPackageService.myUpdate(invPackage));
    }

    @ApiOperation(value = "入库后打包分页查询")
    @PostMapping("/page")
    public Result<IPage<InvPackage>> page(Page page, @RequestBody InvPackage invPackage) {
        IPage<InvPackage> result = invPackageService.myPage(page, invPackage);
        return Result.success(result);
    }

    @ApiOperation(value = "入库后打包分录分页查询")
    @PostMapping("/itemPage")
    public Result<IPage<InvPackageItemVo>> itemPage(Page page, @RequestBody InvPackage invPackage) {
        IPage<InvPackageItemVo> result = invPackageService.itemPage(page, invPackage);
        return Result.success(result);
    }

    @ApiOperation(value = "入库后打包详细信息")
    @GetMapping("/detail/{id}")
    public Result<InvPackage> detail(@PathVariable String id) {
        InvPackage result = invPackageService.detail(id);
        return Result.success(result);
    }

    @ApiOperation(value = "提交")
    @PutMapping("/submit/{id}")
    public Result<InvPackage> submit(@PathVariable String id) {
        InvPackage result = invPackageService.submit(id);
        return Result.success(result);
    }

    @PutMapping("/batchSubmitByIds")
    public Result<String> batchSubmitByIds(@RequestBody String[] ids) {
        String result = invPackageService.batchSubmitByIds(ids);
        return Result.success(result);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "invPackage", paramType = "body", dataType = "InvPackage"),
    })
    @ApiOperation(value = "执行操作 提交/审核/反审核")
    @PostMapping("/doAction")
    public Result<InvPackage> doAction(@RequestBody InvPackage invPackage) throws Exception {
        return invPackageService.doAction(invPackage);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "invPackage", paramType = "body", dataType = "InvPackage"),
    })
    @ApiOperation(value = "批量执行操作 提交/审核/反审核")
    @PostMapping("/batchDoAction")
    public Result<String> batchDoAction(@RequestBody InvPackage invPackage) throws Exception {
        return invPackageService.batchDoAction(invPackage);
    }

    @PutMapping("/unAudit/{id}")
    public Result<InvPackage> unAudit(@PathVariable String id) throws Exception {
        InvPackage result = invPackageService.unAudit(id);
        return Result.success(result);
    }

    @PutMapping(value = "/batchUnAuditByIds")
    public Result<String> batchUnAuditByIds(@RequestBody String[] ids) throws Exception {
        String result = invPackageService.batchUnAuditByIds(ids);
        return Result.success(result);
    }

    @GetMapping(value = "/listPackageNoById/{id}")
    public Result<List<InvPackageNo>> listPackageNoById(@PathVariable("id") String id) throws Exception {
        List<InvPackageNo> result = invPackageService.listPackageNoById(id);
        return Result.success(result);
    }

    @ApiOperation(value = "入库后打包分录导出")
    @GetMapping("/itemExport")
    public void itemExport(HttpServletResponse response, InvPackage invPackage) {
        invPackageService.itemExport(response, invPackage);
    }
}
