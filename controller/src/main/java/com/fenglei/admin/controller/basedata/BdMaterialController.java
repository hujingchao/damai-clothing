package com.fenglei.admin.controller.basedata;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.result.Result;
import com.fenglei.model.basedata.*;
import com.fenglei.service.basedata.BdMaterialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ljw
 */
@Api(tags = "物料管理接口")
@RestController
@RequestMapping("/BdMaterial")
@RequiredArgsConstructor
public class BdMaterialController {

    @Resource
    private BdMaterialService bdMaterialService;

    @ApiOperation(value = "物料分页", notes = "物料分页")
    @GetMapping("/page")
    public Result page(Page page, BdMaterial bdMaterial) throws Exception {
        IPage iPage = bdMaterialService.myPage(page, bdMaterial);
        return Result.success(iPage);
    }

    @ApiOperation(value = "物料列表", notes = "物料列表")
    @GetMapping("/list")
    public Result list(BdMaterial bdMaterial) throws Exception {
        List<BdMaterial> list = bdMaterialService.myList(bdMaterial);
        return Result.success(list);
    }

    @ApiOperation(value = "物料select的过滤", notes = "物料select的过滤")
    @GetMapping("/listForSelect")
    public Result listForSelect(BdMaterial bdMaterial) throws Exception {
        List<BdMaterial> list = bdMaterialService.listForSelect(bdMaterial);
        return Result.success(list);
    }

    @ApiOperation(value = "货品编码列表", notes = "物料列表")
    @GetMapping("/listMtrlNum")
    public Result listMtrlNum(BdMaterial bdMaterial) throws Exception {
        List<BdMaterial> list = bdMaterialService.listMtrlNum(bdMaterial);
        return Result.success(list);
    }

    @GetMapping("/detailMtrlNum")
    public Result detailMtrlNum(BdMaterial bdMaterial) throws Exception {
        BdMaterial result = bdMaterialService.detailMtrlNum(bdMaterial);
        return Result.success(result);
    }

    @ApiOperation(value = "新增物料", notes = "新增物料")
    @PostMapping("/add")
    public Result add(@RequestBody BdMaterial bdMaterial) throws Exception {
        BdMaterial result = bdMaterialService.add(bdMaterial);
        return Result.success(result);
    }

    @ApiOperation(value = "修改物料", notes = "修改物料")
    @PutMapping("/update")
    public Result update(@RequestBody BdMaterial bdMaterial) throws Exception {
        BdMaterial result = bdMaterialService.myUpdate(bdMaterial);
        return Result.success(result);
    }

    @ApiOperation(value = "删除物料", notes = "删除物料")
    @DeleteMapping("/del/{id}")
    public Result delete(@PathVariable String id) throws Exception {
        if (bdMaterialService.deleteById(id)) {
            return Result.success();
        }
        return Result.failed("删除失败");
    }

    @PutMapping(value = "/deleteByIds")
    public Result deleteBatch(@RequestBody String[] ids) {
        bdMaterialService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable String id) throws Exception {
        BdMaterial result = bdMaterialService.detail(id);
        return Result.success(result);
    }

    @PutMapping("/submit/{id}")
    public Result submit(@PathVariable String id) throws Exception {
        BdMaterial result = bdMaterialService.submit(id);
        return Result.success(result);
    }

    @PutMapping(value = "/batchSubmitByIds")
    public Result batchSubmitByIds(@RequestBody String[] ids) throws Exception {
        String result = bdMaterialService.batchSubmitByIds(ids);
        return Result.success(result);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "bdMaterial", paramType = "body", dataType = "BdMaterial"),
    })
    @ApiOperation(value = "执行操作 提交/审核/反审核")
    @PostMapping("/doAction")
    public Result doAction(@RequestBody BdMaterial bdMaterial) throws Exception {
        return bdMaterialService.doAction(bdMaterial);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "bdMaterial", paramType = "body", dataType = "BdMaterial"),
    })
    @ApiOperation(value = "批量执行操作 提交/审核/反审核")
    @PostMapping("/batchDoAction")
    public Result batchDoAction(@RequestBody BdMaterial bdMaterial) throws Exception {
        return bdMaterialService.batchDoAction(bdMaterial);
    }

    @PutMapping("/unAudit/{id}")
    public Result unAudit(@PathVariable String id) throws Exception {
        BdMaterial result = bdMaterialService.unAudit(id);
        return Result.success(result);
    }

    @PutMapping(value = "/batchUnAuditByIds")
    public Result batchUnAuditByIds(@RequestBody String[] ids) throws Exception {
        String result = bdMaterialService.batchUnAuditByIds(ids);
        return Result.success(result);
    }

    @ApiOperation(value = "颜色/色号列表", notes = "颜色/色号列表")
    @GetMapping("/listColor")
    public Result listColor(BdMaterial bdMaterial) {
        List<BdMaterialColor> list = bdMaterialService.listColor(bdMaterial);
        return Result.success(list);
    }

    @ApiOperation(value = "规格列表", notes = "规格列表")
    @GetMapping("/listSpecification")
    public Result listSpecification(BdMaterial bdMaterial) {
        List<BdMaterialSpecification> list = bdMaterialService.listSpecification(bdMaterial);
        return Result.success(list);
    }

    @ApiOperation(value = "供应商列表", notes = "供应商列表")
    @GetMapping("/listSupplier")
    public Result listSupplier(BdMaterial bdMaterial) {
        List<BdMaterialSupplier> list = bdMaterialService.listSupplier(bdMaterial);
        return Result.success(list);
    }


    @ApiOperation(value = "新增成品", notes = "新增成品")
    @PostMapping("/saveFin")
    public Result saveFin(@RequestBody BdMaterial bdMaterial) throws Exception {
        BdMaterial result = bdMaterialService.saveFin(bdMaterial);
        return Result.success(result);
    }

    @ApiOperation(value = "删除物料", notes = "删除物料")
    @DeleteMapping("/delFin/{id}")
    public Result delFin(@PathVariable String id) throws Exception {
        if (bdMaterialService.deleteByIdFin(id)) {
            return Result.success();
        }
        return Result.failed("删除失败");
    }

    @PutMapping(value = "/delByIdsFin")
    public Result delBatchFin(@RequestBody String[] ids) {
        bdMaterialService.deleteBatchFin(ids);
        return Result.success();
    }

    @GetMapping("/detailFin/{id}")
    public Result detailFin(@PathVariable String id) throws Exception {
        BdMaterial result = bdMaterialService.detailFin(id);
        return Result.success(result);
    }

    @PutMapping("/submitFin/{id}")
    public Result submitFin(@PathVariable String id) throws Exception {
        BdMaterialDetail result = bdMaterialService.submitFin(id);
        return Result.success(result);
    }

    @PutMapping(value = "/batchSubmitByIdsFin")
    public Result batchSubmitByIdsFin(@RequestBody String[] ids) throws Exception {
        String result = bdMaterialService.batchSubmitByIdsFin(ids);
        return Result.success(result);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "bdMaterialDetail", paramType = "body", dataType = "BdMaterialDetail"),
    })
    @ApiOperation(value = "执行操作 提交/审核/反审核")
    @PostMapping("/doActionFin")
    public Result doActionFin(@RequestBody BdMaterialDetail bdMaterialDetail) throws Exception {
        return bdMaterialService.doActionFin(bdMaterialDetail);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "bdMaterial", paramType = "body", dataType = "BdMaterial"),
    })
    @ApiOperation(value = "批量执行操作 提交/审核/反审核")
    @PostMapping("/batchDoActionFin")
    public Result batchDoActionFin(@RequestBody BdMaterialDetail bdMaterialDetail) throws Exception {
        return bdMaterialService.batchDoActionFin(bdMaterialDetail);
    }

    @PutMapping("/unAuditFin/{id}")
    public Result unAuditFin(@PathVariable String id) throws Exception {
        BdMaterialDetail result = bdMaterialService.unAuditFin(id);
        return Result.success(result);
    }

    @PutMapping(value = "/batchUnAuditByIdsFin")
    public Result batchUnAuditByIdsFin(@RequestBody String[] ids) throws Exception {
        String result = bdMaterialService.batchUnAuditByIdsFin(ids);
        return Result.success(result);
    }

    @ApiOperation(value = "成品分页", notes = "成品分页")
    @GetMapping("/pageFin")
    public Result pageFin(Page page, BdMaterial bdMaterial) throws Exception {
        IPage iPage = bdMaterialService.myPageFin(page, bdMaterial);
        return Result.success(iPage);
    }

    @ApiOperation(value = "成品列表", notes = "成品列表")
    @GetMapping("/listFin")
    public Result listFin(BdMaterial bdMaterial) throws Exception {
        List<BdMaterial> list = bdMaterialService.myListFin(bdMaterial);
        return Result.success(list);
    }

    @PutMapping(value = "/getRawsFin")
    public Result getRawsFin(@RequestBody BdMaterial bdMaterial) throws Exception {
        List<BdMaterialRaw> list = bdMaterialService.getRawsFin(bdMaterial);
        return Result.success(list);
    }




    @ApiOperation(value = "成品批量更新工价")
    @PostMapping("/updateProcessPrice/{id}")
    public Result updateProcessPrice(@PathVariable String id) throws Exception {
        bdMaterialService.updateProcessPrice(id);
        return Result.success();
    }
}
