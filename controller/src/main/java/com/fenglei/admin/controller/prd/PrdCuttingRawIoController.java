package com.fenglei.admin.controller.prd;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.result.Result;
import com.fenglei.model.prd.entity.PrdCuttingRawIo;
import com.fenglei.service.prd.IPrdCuttingRawIoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 裁床单原材料出库记录 前端控制器
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-15
 */
@Api(tags = "裁床单原材料出库记录 前端控制器")
@RestController
@RequestMapping("/api.admin/v1/prdCuttingRawIo")
@AllArgsConstructor
@Slf4j
public class PrdCuttingRawIoController {
    IPrdCuttingRawIoService prdCuttingRawIoService;

    @ApiOperation(value = "裁床单原材料出库记录新增")
    @PostMapping("/add")
    public Result<PrdCuttingRawIo> add(@RequestBody PrdCuttingRawIo prdCuttingRawIo) {
        PrdCuttingRawIo result = prdCuttingRawIoService.add(prdCuttingRawIo);
        return Result.success(result);
    }

    @ApiOperation(value = "裁床单原材料出库记录删除")
    @DeleteMapping("/removeById/{id}")
    public Result<Boolean> removeById(@PathVariable String id) {
        return Result.judge(prdCuttingRawIoService.myRemoveById(id));
    }

    @ApiOperation(value = "裁床单原材料出库记录批量删除")
    @DeleteMapping("/removeByIds")
    public Result<Boolean> removeByIds(@RequestBody PrdCuttingRawIo prdCuttingRawIo) {
        return Result.judge(prdCuttingRawIoService.myRemoveByIds(prdCuttingRawIo.getIds()));
    }
}
