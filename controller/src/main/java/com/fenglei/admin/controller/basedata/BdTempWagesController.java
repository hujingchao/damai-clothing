package com.fenglei.admin.controller.basedata;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.result.Result;
import com.fenglei.model.basedata.BdTempWages;
import com.fenglei.service.basedata.BdTempWagesService;
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
@Api(tags = "工价管理接口")
@RestController
@RequestMapping("/BdTempWages")
@RequiredArgsConstructor
public class BdTempWagesController {

    @Resource
    private BdTempWagesService bdTempWagesService;

    @ApiOperation(value = "工价分页", notes = "工价分页")
    @GetMapping("/page")
    public Result page(Page page, BdTempWages bdTempWages) throws Exception {
        IPage iPage = bdTempWagesService.myPage(page, bdTempWages);
        return Result.success(iPage);
    }

    @ApiOperation(value = "工价列表", notes = "工价列表")
    @GetMapping("/list")
    public Result list(BdTempWages bdTempWages) throws Exception {
        List<BdTempWages> list = bdTempWagesService.myList(bdTempWages);
        return Result.success(list);
    }

    @ApiOperation(value = "新增工价", notes = "新增工价")
    @PostMapping("/add")
    public Result add(@RequestBody BdTempWages bdTempWages) throws Exception {
        BdTempWages result = bdTempWagesService.add(bdTempWages);
        return Result.success(result);
    }

    @ApiOperation(value = "修改工价", notes = "修改工价")
    @PutMapping("/update")
    public Result update(@RequestBody BdTempWages bdTempWages) throws Exception {
        BdTempWages result = bdTempWagesService.myUpdate(bdTempWages);
        return Result.success(result);
    }

    @ApiOperation(value = "删除工价", notes = "删除工价")
    @DeleteMapping("/del/{id}")
    public Result delete(@PathVariable String id) throws Exception {
        if (bdTempWagesService.deleteById(id)) {
            return Result.success();
        }
        return Result.failed("删除失败");
    }

    @PutMapping(value = "/deleteByIds")
    public Result deleteBatch(@RequestBody String[] ids) {
        bdTempWagesService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable String id) throws Exception {
        BdTempWages result = bdTempWagesService.detail(id);
        return Result.success(result);
    }

    @PutMapping("/submit/{id}")
    public Result submit(@PathVariable String id) throws Exception {
        BdTempWages result = bdTempWagesService.submit(id);
        return Result.success(result);
    }

    @PutMapping(value = "/batchSubmitByIds")
    public Result batchSubmitByIds(@RequestBody String[] ids) throws Exception {
        String result = bdTempWagesService.batchSubmitByIds(ids);
        return Result.success(result);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "prdMo", paramType = "body", dataType = "PrdMo"),
    })
    @ApiOperation(value = "执行操作 提交/审核/反审核")
    @PostMapping("/doAction")
    public Result doAction(@RequestBody BdTempWages bdTempWages) throws Exception {
        return bdTempWagesService.doAction(bdTempWages);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "prdMo", paramType = "body", dataType = "PrdMo"),
    })
    @ApiOperation(value = "批量执行操作 提交/审核/反审核")
    @PostMapping("/batchDoAction")
    public Result batchDoAction(@RequestBody BdTempWages bdTempWages) throws Exception {
        return bdTempWagesService.batchDoAction(bdTempWages);
    }

    @PutMapping("/unAudit/{id}")
    public Result unAudit(@PathVariable String id) throws Exception {
        BdTempWages result = bdTempWagesService.unAudit(id);
        return Result.success(result);
    }

    @PutMapping(value = "/batchUnAuditByIds")
    public Result batchUnAuditByIds(@RequestBody String[] ids) {
        String result = bdTempWagesService.batchUnAuditByIds(ids);
        return Result.success(result);
    }

    @PutMapping("/cancel/{id}")
    public Result cancel(@PathVariable String id) throws Exception {
        BdTempWages result = bdTempWagesService.cancel(id);
        return Result.success(result);
    }

    @PutMapping(value = "/batchCancelByIds")
    public Result batchCancelByIds(@RequestBody String[] ids) throws Exception {
        String result = bdTempWagesService.batchCancelByIds(ids);
        return Result.success(result);
    }

    @PutMapping("/unCancel/{id}")
    public Result unCancel(@PathVariable String id) throws Exception {
        BdTempWages result = bdTempWagesService.unCancel(id);
        return Result.success(result);
    }

    @PutMapping(value = "/batchUnCancelByIds")
    public Result batchUnCancelByIds(@RequestBody String[] ids) throws Exception {
        String result = bdTempWagesService.batchUnCancelByIds(ids);
        return Result.success(result);
    }
}
