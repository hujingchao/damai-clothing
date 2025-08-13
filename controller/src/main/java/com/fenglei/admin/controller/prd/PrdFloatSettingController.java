package com.fenglei.admin.controller.prd;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.result.Result;
import com.fenglei.model.prd.entity.PrdFloatSetting;
import com.fenglei.service.prd.IPrdFloatSettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zgm
 * @since 2024-04-18
 */
@Api(tags = " 前端控制器")
@RestController
@RequestMapping("/api.admin/v1/prdFloatSetting")
@AllArgsConstructor
@Slf4j
public class PrdFloatSettingController {
    IPrdFloatSettingService prdFloatSettingService;

    @ApiOperation(value = "新增")
    @PostMapping("/add")
    public Result<PrdFloatSetting> add(@RequestBody PrdFloatSetting prdFloatSetting) {
        PrdFloatSetting result = prdFloatSettingService.add(prdFloatSetting);
        return Result.success(result);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("/removeById/{id}")
    public Result<Boolean> removeById(@PathVariable String id) {
        return Result.judge(prdFloatSettingService.myRemoveById(id));
    }

    @ApiOperation(value = "批量删除")
    @DeleteMapping("/removeByIds")
    public Result<Boolean> removeByIds(@RequestBody PrdFloatSetting prdFloatSetting) {
        return Result.judge(prdFloatSettingService.myRemoveByIds(prdFloatSetting.getIds()));
    }

    @ApiOperation(value = "修改")
    @PutMapping("/update")
    public Result<Boolean> update(@RequestBody PrdFloatSetting prdFloatSetting) {
        return Result.judge(prdFloatSettingService.myUpdate(prdFloatSetting));
    }

    @ApiOperation(value = "分页查询")
    @GetMapping("/page")
    public Result<IPage<PrdFloatSetting>> page(Page page, PrdFloatSetting prdFloatSetting) {
        IPage<PrdFloatSetting> result = prdFloatSettingService.myPage(page, prdFloatSetting);
        return Result.success(result);
    }

    @ApiOperation(value = "详细信息")
    @GetMapping("/detail/{id}")
    public Result<PrdFloatSetting> detail(@PathVariable String id) {
        PrdFloatSetting result = prdFloatSettingService.detail(id);
        return Result.success(result);
    }

    @ApiOperation(value = "获取生产浮动设置参数")
    @GetMapping("/getSetting")
    public Result<PrdFloatSetting> getSetting() {
        PrdFloatSetting result = prdFloatSettingService.getSetting();
        return Result.success(result);
    }
}
