package com.fenglei.admin.controller.pur;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.result.Result;
import com.fenglei.model.pur.entity.PurPurchaseInstock;
import com.fenglei.model.pur.entity.PurPurchaseOrder;
import com.fenglei.service.pur.PurPurchaseInstockService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author ljw
 */
@Api(tags = "采购入库管理接口")
@RestController
@RequestMapping("/PurPurchaseInstock")
@RequiredArgsConstructor
public class PurPurchaseInstockController {

    @Resource
    private PurPurchaseInstockService purPurchaseInstockService;

    @ApiOperation(value = "采购入库分页", notes = "采购入库分页")
    @GetMapping("/page")
    public Result page(Page page, PurPurchaseInstock purPurchaseInstock) throws Exception {
        IPage iPage = purPurchaseInstockService.myPage(page, purPurchaseInstock);
        return Result.success(iPage);
    }

    @ApiOperation(value = "采购入库列表", notes = "采购入库列表")
    @GetMapping("/list")
    public Result page(PurPurchaseInstock purPurchaseInstock) throws Exception {
        List<PurPurchaseInstock> list = purPurchaseInstockService.myList(purPurchaseInstock);
        return Result.success(list);
    }

    @ApiOperation(value = "新增采购入库", notes = "新增采购入库")
    @PostMapping("/add")
    public Result add(@RequestBody PurPurchaseInstock purPurchaseInstock) throws Exception {
        PurPurchaseInstock result = purPurchaseInstockService.add(purPurchaseInstock);
        return Result.success(result);
    }

    @ApiOperation(value = "修改采购入库", notes = "修改采购入库")
    @PutMapping("/update")
    public Result update(@RequestBody PurPurchaseInstock purPurchaseInstock) throws Exception {
        PurPurchaseInstock result = purPurchaseInstockService.myUpdate(purPurchaseInstock);
        return Result.success(result);
    }

    @ApiOperation(value = "删除采购入库", notes = "删除采购入库")
    @DeleteMapping("/del/{id}")
    public Result delete(@PathVariable Long id) throws Exception {
        if (purPurchaseInstockService.deleteById(id)) {
            return Result.success();
        }
        return Result.failed("删除失败");
    }

    @PutMapping(value = "/deleteByIds")
    public Result deleteBatch(@RequestBody String[] ids) {
        purPurchaseInstockService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable String id) throws Exception {
        PurPurchaseInstock result = purPurchaseInstockService.detail(id);
        return Result.success(result);
    }

    @PutMapping("/submit/{id}")
    public Result submit(@PathVariable String id) throws Exception {
        PurPurchaseInstock result = purPurchaseInstockService.submit(id);
        return Result.success(result);
    }

    @PutMapping(value = "/batchSubmitByIds")
    public Result batchSubmitByIds(@RequestBody String[] ids) throws Exception {
        String result = purPurchaseInstockService.batchSubmitByIds(ids);
        return Result.success(result);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "purPurchaseInstock", paramType = "body", dataType = "PurPurchaseInstock"),
    })
    @ApiOperation(value = "执行操作 提交/审核/反审核")
    @PostMapping("/doAction")
    public Result doAction(@RequestBody PurPurchaseInstock purPurchaseInstock) throws Exception {
        return purPurchaseInstockService.doAction(purPurchaseInstock);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "purPurchaseInstock", paramType = "body", dataType = "PurPurchaseInstock"),
    })
    @ApiOperation(value = "批量执行操作 提交/审核/反审核")
    @PostMapping("/batchDoAction")
    public Result batchDoAction(@RequestBody PurPurchaseInstock purPurchaseInstock) throws Exception {
        return purPurchaseInstockService.batchDoAction(purPurchaseInstock);
    }

    @PutMapping("/unAudit/{id}")
    public Result unAudit(@PathVariable String id) throws Exception {
        PurPurchaseInstock result = purPurchaseInstockService.unAudit(id);
        return Result.success(result);
    }

    @PutMapping(value = "/batchUnAuditByIds")
    public Result batchUnAuditByIds(@RequestBody String[] ids) throws Exception {
        String result = purPurchaseInstockService.batchUnAuditByIds(ids);
        return Result.success(result);
    }

    @RequestMapping(value = "/itemExport", method = RequestMethod.GET)
    @ResponseBody
    public void itemExport(HttpServletResponse response, PurPurchaseInstock purchaseInstock) throws Exception {
        purPurchaseInstockService.itemExport(response, purchaseInstock);
    }
}
