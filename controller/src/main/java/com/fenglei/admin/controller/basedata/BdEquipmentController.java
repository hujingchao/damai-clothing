package com.fenglei.admin.controller.basedata;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.result.Result;
import com.fenglei.model.basedata.BdEquipment;
import com.fenglei.service.basedata.IBdEquipmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 设备 前端控制器
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-09
 */
@Api(tags = "设备 前端控制器")
@RestController
@RequestMapping("/api.admin/v1/bdEquipment")
@AllArgsConstructor
@Slf4j
public class BdEquipmentController {
    IBdEquipmentService bdEquipmentService;

    @ApiOperation(value = "设备新增")
    @PostMapping("/add")
    public Result<BdEquipment> add(@RequestBody BdEquipment bdEquipment) {
        BdEquipment result = bdEquipmentService.add(bdEquipment);
        return Result.success(result);
    }

    @ApiOperation(value = "设备删除")
    @DeleteMapping("/removeById/{id}")
    public Result<Boolean> removeById(@PathVariable String id) {
        return Result.judge(bdEquipmentService.myRemoveById(id));
    }

    @ApiOperation(value = "设备批量删除")
    @DeleteMapping("/removeByIds")
    public Result<Boolean> removeByIds(@RequestBody BdEquipment bdEquipment) {
        return Result.judge(bdEquipmentService.myRemoveByIds(bdEquipment.getIds()));
    }

    @ApiOperation(value = "设备修改")
    @PutMapping("/update")
    public Result<BdEquipment> update(@RequestBody BdEquipment bdEquipment) {
        return Result.success(bdEquipmentService.myUpdate(bdEquipment));
    }

    @ApiOperation(value = "设备分页查询")
    @GetMapping("/page")
    public Result<IPage<BdEquipment>> page(Page page, BdEquipment bdEquipment) {
        IPage<BdEquipment> result = bdEquipmentService.myPage(page, bdEquipment);
        return Result.success(result);
    }

    @ApiOperation(value = "设备查询")
    @GetMapping("/list")
    public Result<List<BdEquipment>> list(BdEquipment bdEquipment) {
        List<BdEquipment> result = bdEquipmentService.myList(bdEquipment);
        return Result.success(result);
    }

    @ApiOperation(value = "设备详细信息")
    @GetMapping("/detail/{id}")
    public Result<BdEquipment> detail(@PathVariable String id) {
        BdEquipment result = bdEquipmentService.detail(id);
        return Result.success(result);
    }
}
