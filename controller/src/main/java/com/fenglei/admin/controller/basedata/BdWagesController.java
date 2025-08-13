package com.fenglei.admin.controller.basedata;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.result.Result;
import com.fenglei.model.basedata.BdWages;
import com.fenglei.service.basedata.BdWagesService;
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
@RequestMapping("/BdWages")
@RequiredArgsConstructor
public class BdWagesController {

    @Resource
    private BdWagesService bdWagesService;

    @ApiOperation(value = "工价分页", notes = "工价分页")
    @GetMapping("/page")
    public Result page(Page page, BdWages bdWages) throws Exception {
        IPage iPage = bdWagesService.myPage(page, bdWages);
        return Result.success(iPage);
    }

    @ApiOperation(value = "工价分页", notes = "工价分页")
    @GetMapping("/pageWithTemp")
    public Result pageWithTemp(Page page, BdWages bdWages) throws Exception {
        IPage iPage = bdWagesService.myPageWithTemp(page, bdWages);
        return Result.success(iPage);
    }

    @ApiOperation(value = "工价列表", notes = "工价列表")
    @GetMapping("/list")
    public Result list(BdWages bdWages) throws Exception {
        List<BdWages> list = bdWagesService.myList(bdWages);
        return Result.success(list);
    }

    @ApiOperation(value = "新增工价", notes = "新增工价")
    @PostMapping("/add")
    public Result add(@RequestBody BdWages bdWages) throws Exception {
        BdWages result = bdWagesService.add(bdWages);
        return Result.success(result);
    }

    @ApiOperation(value = "修改工价", notes = "修改工价")
    @PutMapping("/update")
    public Result update(@RequestBody BdWages bdWages) throws Exception {
        BdWages result = bdWagesService.myUpdate(bdWages);
        return Result.success(result);
    }

    @ApiOperation(value = "删除工价", notes = "删除工价")
    @DeleteMapping("/del/{id}")
    public Result delete(@PathVariable Long id) throws Exception {
        if (bdWagesService.deleteById(id)) {
            return Result.success();
        }
        return Result.failed("删除失败");
    }

    @PutMapping(value = "/deleteByIds")
    public Result deleteBatch(@RequestBody String[] ids) {
        bdWagesService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable String id) throws Exception {
        BdWages result = bdWagesService.detail(id);
        return Result.success(result);
    }

    @PutMapping("/submit/{id}")
    public Result submit(@PathVariable String id) throws Exception {
        BdWages result = bdWagesService.submit(id);
        return Result.success(result);
    }

    @PutMapping(value = "/batchSubmitByIds")
    public Result batchSubmitByIds(@RequestBody String[] ids) throws Exception {
        String result = bdWagesService.batchSubmitByIds(ids);
        return Result.success(result);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "prdMo", paramType = "body", dataType = "PrdMo"),
    })
    @ApiOperation(value = "执行操作 提交/审核/反审核")
    @PostMapping("/doAction")
    public Result doAction(@RequestBody BdWages bdWages) throws Exception {
        return bdWagesService.doAction(bdWages);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "prdMo", paramType = "body", dataType = "PrdMo"),
    })
    @ApiOperation(value = "批量执行操作 提交/审核/反审核")
    @PostMapping("/batchDoAction")
    public Result batchDoAction(@RequestBody BdWages bdWages) throws Exception {
        return bdWagesService.batchDoAction(bdWages);
    }

    @PutMapping("/unAudit/{id}")
    public Result unAudit(@PathVariable String id) throws Exception {
        BdWages result = bdWagesService.unAudit(id);
        return Result.success(result);
    }

    @PutMapping(value = "/batchUnAuditByIds")
    public Result batchUnAuditByIds(@RequestBody String[] ids) throws Exception {
        String result = bdWagesService.batchUnAuditByIds(ids);
        return Result.success(result);
    }

    @PutMapping("/cancel/{id}")
    public Result cancel(@PathVariable String id) throws Exception {
        BdWages result = bdWagesService.cancel(id);
        return Result.success(result);
    }

    @PutMapping(value = "/batchCancelByIds")
    public Result batchCancelByIds(@RequestBody String[] ids) throws Exception {
        String result = bdWagesService.batchCancelByIds(ids);
        return Result.success(result);
    }

    @PutMapping("/unCancel/{id}")
    public Result unCancel(@PathVariable String id) throws Exception {
        BdWages result = bdWagesService.unCancel(id);
        return Result.success(result);
    }

    @PutMapping(value = "/batchUnCancelByIds")
    public Result batchUnCancelByIds(@RequestBody String[] ids) throws Exception {
        String result = bdWagesService.batchUnCancelByIds(ids);
        return Result.success(result);
    }
}
