package com.fenglei.admin.controller.basedata;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.result.Result;
import com.fenglei.model.basedata.BdTempMaterial;
import com.fenglei.model.basedata.BdTempMaterialColor;
import com.fenglei.model.basedata.BdTempMaterialSpecification;
import com.fenglei.service.basedata.BdTempMaterialService;
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
@RequestMapping("/BdTempMaterial")
@RequiredArgsConstructor
public class BdTempMaterialController {

    @Resource
    private BdTempMaterialService bdTempMaterialService;

    @ApiOperation(value = "物料分页", notes = "物料分页")
    @GetMapping("/page")
    public Result page(Page page, BdTempMaterial bdMaterial) throws Exception {
        IPage iPage = bdTempMaterialService.myPage(page, bdMaterial);
        return Result.success(iPage);
    }

    @ApiOperation(value = "物料列表", notes = "物料列表")
    @GetMapping("/list")
    public Result list(BdTempMaterial bdMaterial) throws Exception {
        List<BdTempMaterial> list = bdTempMaterialService.myList(bdMaterial);
        return Result.success(list);
    }

    @ApiOperation(value = "新增物料", notes = "新增物料")
    @PostMapping("/add")
    public Result add(@RequestBody BdTempMaterial bdMaterial) throws Exception {
        BdTempMaterial result = bdTempMaterialService.add(bdMaterial);
        return Result.success(result);
    }

    @ApiOperation(value = "新增成品", notes = "新增成品")
    @PostMapping("/saveFin")
    public Result saveFin(@RequestBody BdTempMaterial bdMaterial) throws Exception {
        BdTempMaterial result = bdTempMaterialService.saveFin(bdMaterial);
        return Result.success(result);
    }

    @ApiOperation(value = "修改物料", notes = "修改物料")
    @PutMapping("/update")
    public Result update(@RequestBody BdTempMaterial bdMaterial) throws Exception {
        BdTempMaterial result = bdTempMaterialService.myUpdate(bdMaterial);
        return Result.success(result);
    }

    @ApiOperation(value = "删除物料", notes = "删除物料")
    @DeleteMapping("/del/{id}")
    public Result delete(@PathVariable String id) throws Exception {
        if (bdTempMaterialService.deleteById(id)) {
            return Result.success();
        }
        return Result.failed("删除失败");
    }

    @PutMapping(value = "/deleteByIds")
    public Result deleteBatch(@RequestBody String[] ids) {
        bdTempMaterialService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable String id) throws Exception {
        BdTempMaterial result = bdTempMaterialService.detail(id);
        return Result.success(result);
    }

    @PutMapping("/submit/{id}")
    public Result submit(@PathVariable String id) throws Exception {
        BdTempMaterial result = bdTempMaterialService.submit(id);
        return Result.success(result);
    }

    @PutMapping(value = "/batchSubmitByIds")
    public Result batchSubmitByIds(@RequestBody String[] ids) throws Exception {
        String result = bdTempMaterialService.batchSubmitByIds(ids);
        return Result.success(result);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "bdMaterial", paramType = "body", dataType = "BdTempMaterial"),
    })
    @ApiOperation(value = "执行操作 提交/审核/反审核")
    @PostMapping("/doAction")
    public Result doAction(@RequestBody BdTempMaterial bdMaterial) throws Exception {
        return bdTempMaterialService.doAction(bdMaterial);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "bdMaterial", paramType = "body", dataType = "BdTempMaterial"),
    })
    @ApiOperation(value = "批量执行操作 提交/审核/反审核")
    @PostMapping("/batchDoAction")
    public Result batchDoAction(@RequestBody BdTempMaterial bdMaterial) throws Exception {
        return bdTempMaterialService.batchDoAction(bdMaterial);
    }

    @PutMapping("/unAudit/{id}")
    public Result unAudit(@PathVariable String id) throws Exception {
        BdTempMaterial result = bdTempMaterialService.unAudit(id);
        return Result.success(result);
    }

    @PutMapping(value = "/batchUnAuditByIds")
    public Result batchUnAuditByIds(@RequestBody String[] ids) {
        String result = bdTempMaterialService.batchUnAuditByIds(ids);
        return Result.success(result);
    }

    @ApiOperation(value = "颜色/色号列表", notes = "颜色/色号列表")
    @GetMapping("/listColor")
    public Result listColor(BdTempMaterial bdMaterial) {
        List<BdTempMaterialColor> list = bdTempMaterialService.listColor(bdMaterial);
        return Result.success(list);
    }

    @ApiOperation(value = "规格列表", notes = "规格列表")
    @GetMapping("/listSpecification")
    public Result listSpecification(BdTempMaterial bdMaterial) {
        List<BdTempMaterialSpecification> list = bdTempMaterialService.listSpecification(bdMaterial);
        return Result.success(list);
    }
}
