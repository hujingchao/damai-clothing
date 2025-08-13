package com.fenglei.admin.controller.prd;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.result.Result;
import com.fenglei.model.prd.entity.PrdCuttingRoute;
import com.fenglei.model.prd.vo.CuttingRouteDetailVo;
import com.fenglei.service.prd.IPrdCuttingRouteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 裁床单 - 工序 前端控制器
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-03
 */
@Api(tags = "裁床单 - 工序 前端控制器")
@RestController
@RequestMapping("/api.admin/v1/prdCuttingRoute")
@AllArgsConstructor
@Slf4j
public class PrdCuttingRouteController {
    IPrdCuttingRouteService prdCuttingRouteService;

    @ApiOperation(value = "裁床单 - 工序新增")
    @PostMapping("/add")
    public Result<PrdCuttingRoute> add(@RequestBody PrdCuttingRoute prdCuttingRoute) {
        PrdCuttingRoute result = prdCuttingRouteService.add(prdCuttingRoute);
        return Result.success(result);
    }

    @ApiOperation(value = "裁床单 - 工序删除")
    @DeleteMapping("/removeById/{id}")
    public Result removeById(@PathVariable String id) {
        return Result.judge(prdCuttingRouteService.myRemoveById(id));
    }

    @ApiOperation(value = "裁床单 - 工序批量删除")
    @DeleteMapping("/removeByIds")
    public Result removeByIds(@RequestBody PrdCuttingRoute prdCuttingRoute) {
        return Result.judge(prdCuttingRouteService.myRemoveByIds(prdCuttingRoute.getIds()));
    }

    @ApiOperation(value = "裁床单 - 工序修改")
    @PutMapping("/update")
    public Result update(@RequestBody PrdCuttingRoute prdCuttingRoute) {
        return Result.judge(prdCuttingRouteService.myUpdate(prdCuttingRoute));
    }

    @ApiOperation(value = "裁床单 - 工序详细信息")
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable String id) {
        PrdCuttingRoute result = prdCuttingRouteService.detail(id);
        return Result.success(result);
    }
}
