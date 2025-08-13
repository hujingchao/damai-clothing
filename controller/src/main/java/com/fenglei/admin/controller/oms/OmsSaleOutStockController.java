package com.fenglei.admin.controller.oms;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.result.Result;
import com.fenglei.model.oms.entity.OmsSaleOutStock;
import com.fenglei.model.oms.entity.dto.OmsSaleOutStockDTO;
import com.fenglei.model.prd.dto.CuttingFilterDto;
import com.fenglei.model.prd.vo.PrdPackingItemVo;
import com.fenglei.service.oms.IOmsSaleOutStockService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 销售出库单 前端控制器
 * </p>
 *
 * @author zjn
 * @since 2024-04-15
 */
@Api(tags = "销售出库单 前端控制器")
@RestController
@RequestMapping("/api.admin/v1/omsSaleOutStock")
@AllArgsConstructor
@Slf4j
public class OmsSaleOutStockController {
    IOmsSaleOutStockService omsSaleOutStockService;

    @ApiOperation(value = "销售出库单新增")
    @PostMapping("/add")
    public Result<OmsSaleOutStock> add(@RequestBody OmsSaleOutStock omsSaleOutStock) {
        OmsSaleOutStock result = omsSaleOutStockService.add(omsSaleOutStock);
        return Result.success(result);
    }

    @ApiOperation(value = "销售出库单删除")
    @DeleteMapping("/removeById/{id}")
    public Result<Boolean> removeById(@PathVariable String id) {
        return Result.judge(omsSaleOutStockService.myRemoveById(id));
    }

    @ApiOperation(value = "销售出库单批量删除")
    @DeleteMapping("/removeByIds")
    public Result<Boolean> removeByIds(@RequestBody List<String> ids) {
        return Result.judge(omsSaleOutStockService.myRemoveByIds(ids));
    }

    @ApiOperation(value = "销售出库单修改")
    @PutMapping("/update")
    public Result<Boolean> update(@RequestBody OmsSaleOutStock omsSaleOutStock) {
        return Result.judge(omsSaleOutStockService.myUpdate(omsSaleOutStock));
    }

    @ApiOperation(value = "销售出库单分页查询")
    @GetMapping("/page")
    public Result<IPage<OmsSaleOutStock>> page(Page page, OmsSaleOutStock omsSaleOutStock) {
        IPage<OmsSaleOutStock> result = omsSaleOutStockService.myPage(page, omsSaleOutStock);
        return Result.success(result);
    }

    @ApiOperation(value = "销售出库单详细信息")
    @GetMapping("/detail/{id}")
    public Result<OmsSaleOutStock> detail(@PathVariable String id) {
        OmsSaleOutStock result = omsSaleOutStockService.detail(id);
        return Result.success(result);
    }

    @PutMapping("/submit/{id}")
    public Result submit(@PathVariable String id) throws Exception {
        OmsSaleOutStock result = omsSaleOutStockService.submit(id);
        return Result.success(result);
    }

    @PutMapping(value = "/batchSubmitByIds")
    public Result batchSubmitByIds(@RequestBody String[] ids) throws Exception {
        String result = omsSaleOutStockService.batchSubmitByIds(ids);
        return Result.success(result);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "omsSaleOutStock", paramType = "body", dataType = "OmsSaleOutStock"),
    })
    @ApiOperation(value = "执行操作 提交/审核/反审核")
    @PostMapping("/doAction")
    public Result doAction(@RequestBody OmsSaleOutStock omsSaleOutStock) throws Exception {
        return omsSaleOutStockService.doAction(omsSaleOutStock);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "omsSaleOutStock", paramType = "body", dataType = "OmsSaleOutStock"),
    })
    @ApiOperation(value = "批量执行操作 提交/审核/反审核")
    @PostMapping("/batchDoAction")
    public Result batchDoAction(@RequestBody OmsSaleOutStock omsSaleOutStock) throws Exception {
        return omsSaleOutStockService.batchDoAction(omsSaleOutStock);
    }

    @PutMapping("/unAudit/{id}")
    public Result unAudit(@PathVariable String id) throws Exception {
        OmsSaleOutStock result = omsSaleOutStockService.unAudit(id);
        return Result.success(result);
    }

    @PutMapping(value = "/batchUnAuditByIds")
    public Result batchUnAuditByIds(@RequestBody String[] ids) throws Exception {
        String result = omsSaleOutStockService.batchUnAuditByIds(ids);
        return Result.success(result);
    }

    @ApiOperation(value = "状态数量查询")
    @GetMapping("/getStatusCountAll")
    public Result<List<Integer>> getStatusCountAll(OmsSaleOutStock omsSaleOutStock) {
        List<Integer> result = omsSaleOutStockService.getStatusCountAll(omsSaleOutStock);
        return Result.success(result);
    }

    @ApiOperation(value = "销售出库汇总表")
    @GetMapping("/getOutStockSummary")
    public Result<IPage<OmsSaleOutStockDTO>> getOutStockSummary(Page page, OmsSaleOutStockDTO omsSaleOutStockDTO) {
        IPage<OmsSaleOutStockDTO> result = omsSaleOutStockService.getOutStockSummary(page, omsSaleOutStockDTO);
        return Result.success(result);
    }

    @ApiOperation(value = "销售出库汇总表导出")
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/exportOutStockSummary")
    public void exportOutStockSummary(HttpServletResponse response, @RequestBody OmsSaleOutStockDTO omsSaleOutStockDTO) throws Exception {
        omsSaleOutStockService.exportOutStockSummary(response, omsSaleOutStockDTO);
    }

}
