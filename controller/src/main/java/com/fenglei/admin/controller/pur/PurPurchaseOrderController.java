package com.fenglei.admin.controller.pur;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.result.Result;
import com.fenglei.common.util.ExcelUtils;
import com.fenglei.model.pur.entity.PurPurchaseOrder;
import com.fenglei.model.pur.vo.PurPurchaseOrderItemVo;
import com.fenglei.model.report.dto.MaterialsReportDto;
import com.fenglei.model.report.vo.MaterialsReportVo;
import com.fenglei.service.pur.PurPurchaseOrderService;
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
@Api(tags = "采购订单管理接口")
@RestController
@RequestMapping("/PurPurchaseOrder")
@RequiredArgsConstructor
public class PurPurchaseOrderController {

    @Resource
    private PurPurchaseOrderService purPurchaseOrderService;

    @ApiOperation(value = "采购订单分页", notes = "采购订单分页")
    @GetMapping("/page")
    public Result page(Page page, PurPurchaseOrder purPurchaseOrder) throws Exception {
        IPage iPage = purPurchaseOrderService.myPage(page, purPurchaseOrder);
        return Result.success(iPage);
    }

    @ApiOperation(value = "采购订单列表", notes = "采购订单列表")
    @GetMapping("/list")
    public Result page(PurPurchaseOrder purPurchaseOrder) throws Exception {
        List<PurPurchaseOrder> list = purPurchaseOrderService.myList(purPurchaseOrder);
        return Result.success(list);
    }

    @ApiOperation(value = "新增采购订单", notes = "新增采购订单")
    @PostMapping("/add")
    public Result add(@RequestBody PurPurchaseOrder purPurchaseOrder) throws Exception {
        PurPurchaseOrder result = purPurchaseOrderService.add(purPurchaseOrder);
        return Result.success(result);
    }

    @ApiOperation(value = "修改采购订单", notes = "修改采购订单")
    @PutMapping("/update")
    public Result update(@RequestBody PurPurchaseOrder purPurchaseOrder) throws Exception {
        PurPurchaseOrder result = purPurchaseOrderService.myUpdate(purPurchaseOrder);
        return Result.success(result);
    }

    @ApiOperation(value = "删除采购订单", notes = "删除采购订单")
    @DeleteMapping("/del/{id}")
    public Result delete(@PathVariable Long id) throws Exception {
        if (purPurchaseOrderService.deleteById(id)) {
            return Result.success();
        }
        return Result.failed("删除失败");
    }

    @PutMapping(value = "/deleteByIds")
    public Result deleteBatch(@RequestBody String[] ids) {
        purPurchaseOrderService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable String id) throws Exception {
        PurPurchaseOrder result = purPurchaseOrderService.detail(id);
        return Result.success(result);
    }

    @PutMapping("/submit/{id}")
    public Result submit(@PathVariable String id) throws Exception {
        PurPurchaseOrder result = purPurchaseOrderService.submit(id);
        return Result.success(result);
    }

    @PutMapping(value = "/batchSubmitByIds")
    public Result batchSubmitByIds(@RequestBody String[] ids) throws Exception {
        String result = purPurchaseOrderService.batchSubmitByIds(ids);
        return Result.success(result);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "purPurchaseOrder", paramType = "body", dataType = "PurPurchaseOrder"),
    })
    @ApiOperation(value = "执行操作 提交/审核/反审核")
    @PostMapping("/doAction")
    public Result doAction(@RequestBody PurPurchaseOrder purPurchaseOrder) throws Exception {
        return purPurchaseOrderService.doAction(purPurchaseOrder);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "purPurchaseOrder", paramType = "body", dataType = "PurPurchaseOrder"),
    })
    @ApiOperation(value = "批量执行操作 提交/审核/反审核")
    @PostMapping("/batchDoAction")
    public Result batchDoAction(@RequestBody PurPurchaseOrder purPurchaseOrder) throws Exception {
        return purPurchaseOrderService.batchDoAction(purPurchaseOrder);
    }

    @PutMapping("/unAudit/{id}")
    public Result unAudit(@PathVariable String id) throws Exception {
        PurPurchaseOrder result = purPurchaseOrderService.unAudit(id);
        return Result.success(result);
    }

    @PutMapping(value = "/batchUnAuditByIds")
    public Result batchUnAuditByIds(@RequestBody String[] ids) throws Exception {
        String result = purPurchaseOrderService.batchUnAuditByIds(ids);
        return Result.success(result);
    }


    @RequestMapping(value = "/itemExport", method = RequestMethod.GET)
    @ResponseBody
    public void itemExport(HttpServletResponse response, PurPurchaseOrder purchaseOrder) throws Exception {
         purPurchaseOrderService.itemExport(response, purchaseOrder);
    }
}
