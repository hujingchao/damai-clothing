package com.fenglei.admin.controller.prd;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.annotation.SysLogAnnotation;
import com.fenglei.common.result.Result;
import com.fenglei.model.prd.entity.PrdMo;
import com.fenglei.service.prd.PrdMoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author ljw
 */
@Api(tags = "生产订单管理接口")
@RestController
@RequestMapping("/PrdMo")
@RequiredArgsConstructor
public class PrdMoController {

    @Resource
    private PrdMoService prdMoService;

    @ApiOperation(value = "生产订单分页", notes = "生产订单分页")
    @GetMapping("/page")
    public Result page(Page page, PrdMo prdMo) throws Exception {
        IPage iPage = prdMoService.myPage(page, prdMo);
        return Result.success(iPage);
    }

    @ApiOperation(value = "生产订单列表汇总数据", notes = "生产订单列表汇总数据")
    @GetMapping("/getPageSummaryData")
    public Result<PrdMo> getPageSummaryData(PrdMo prdMo) throws Exception {
        return Result.success(prdMoService.getPageSummaryData(prdMo));
    }

    @ApiOperation(value = "生产订单列表", notes = "生产订单列表")
    @GetMapping("/list")
    public Result page(PrdMo prdMo) throws Exception {
        List<PrdMo> list = prdMoService.myList(prdMo);
        return Result.success(list);
    }

    @ApiOperation(value = "新增生产订单", notes = "新增生产订单")
    @PostMapping("/add")
    @SysLogAnnotation("新增生产订单")
    public Result add(@RequestBody PrdMo prdMo) throws Exception {
        PrdMo result = prdMoService.add(prdMo);
        return Result.success(result);
    }

    @ApiOperation(value = "修改生产订单", notes = "修改生产订单")
    @PutMapping("/update")
    public Result update(@RequestBody PrdMo prdMo) throws Exception {
        PrdMo result = prdMoService.myUpdate(prdMo);
        return Result.success(result);
    }

    @ApiOperation(value = "删除生产订单", notes = "删除生产订单")
    @DeleteMapping("/del/{id}")
    public Result delete(@PathVariable Long id) throws Exception {
        if (prdMoService.deleteById(id)) {
            return Result.success();
        }
        return Result.failed("删除失败");
    }

    @PutMapping(value = "/deleteByIds")
    public Result deleteBatch(@RequestBody String[] ids) {
        prdMoService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable String id) throws Exception {
        PrdMo result = prdMoService.detail(id);
        return Result.success(result);
    }

    @PutMapping("/submit/{id}")
    public Result submit(@PathVariable String id) throws Exception {
        PrdMo result = prdMoService.submit(id);
        return Result.success(result);
    }

    @PutMapping(value = "/batchSubmitByIds")
    public Result batchSubmitByIds(@RequestBody String[] ids) throws Exception {
        String result = prdMoService.batchSubmitByIds(ids);
        return Result.success(result);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "prdMo", paramType = "body", dataType = "PrdMo"),
    })
    @ApiOperation(value = "执行操作 提交/审核/反审核")
    @PostMapping("/doAction")
    public Result doAction(@RequestBody PrdMo prdMo) throws Exception {
        return prdMoService.doAction(prdMo);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "prdMo", paramType = "body", dataType = "PrdMo"),
    })
    @ApiOperation(value = "批量执行操作 提交/审核/反审核")
    @PostMapping("/batchDoAction")
    public Result batchDoAction(@RequestBody PrdMo prdMo) throws Exception {
        return prdMoService.batchDoAction(prdMo);
    }

    @PutMapping("/unAudit/{id}")
    public Result unAudit(@PathVariable String id) throws Exception {
        PrdMo result = prdMoService.unAudit(id);
        return Result.success(result);
    }

    @PutMapping(value = "/batchUnAuditByIds")
    public Result batchUnAuditByIds(@RequestBody String[] ids) throws Exception {
        String result = prdMoService.batchUnAuditByIds(ids);
        return Result.success(result);
    }

    @PutMapping("/top/{id}")
    public Result top(@PathVariable String id) {
        PrdMo result = prdMoService.top(id);
        return Result.success(result);
    }

    @PutMapping("/unTop/{id}")
    public Result unTop(@PathVariable String id) {
        PrdMo result = prdMoService.unTop(id);
        return Result.success(result);
    }

    @ApiOperation(value = "关闭订单")
    @PutMapping("/closeOrder/{id}")
    public Result<Boolean> closeOrder(@PathVariable String id) {
        return Result.judge(prdMoService.closeOrder(id));
    }

    @ApiOperation(value = "生产订单导出")
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/exportPrdMo")
    public void exportPrdMo(HttpServletResponse response, @RequestBody PrdMo prdMo) throws Exception {
        prdMoService.exportPrdMo(response, prdMo);
    }
}
