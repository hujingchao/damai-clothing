package com.fenglei.admin.controller.basedata;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.result.Result;
import com.fenglei.model.basedata.BdSpecification;
import com.fenglei.service.basedata.BdSpecificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ljw
 */
@Api(tags = "规格管理接口")
@RestController
@RequestMapping("/BdSpecification")
@RequiredArgsConstructor
public class BdSpecificationController {

    @Resource
    private BdSpecificationService bdSpecificationService;

    @ApiOperation(value = "规格分页", notes = "规格分页")
    @GetMapping("/page")
    public Result page(Page page, BdSpecification bdSpecification) throws Exception {
        IPage iPage = bdSpecificationService.myPage(page, bdSpecification);
        return Result.success(iPage);
    }

    @ApiOperation(value = "规格列表", notes = "规格列表")
    @GetMapping("/list")
    public Result list(BdSpecification bdSpecification) throws Exception {
        List<BdSpecification> list = bdSpecificationService.myList(bdSpecification);
        return Result.success(list);
    }

    @ApiOperation(value = "新增规格", notes = "新增规格")
    @PostMapping("/add")
    public Result add(@RequestBody BdSpecification bdSpecification) throws Exception {
        BdSpecification result = bdSpecificationService.add(bdSpecification);
        return Result.success(result);
    }

    @ApiOperation(value = "修改规格", notes = "修改规格")
    @PutMapping("/update")
    public Result update(@RequestBody BdSpecification bdSpecification) throws Exception {
        BdSpecification result = bdSpecificationService.myUpdate(bdSpecification);
        return Result.success(result);
    }

    @ApiOperation(value = "删除规格", notes = "删除规格")
    @DeleteMapping("/del/{id}")
    public Result delete(@PathVariable Long id) throws Exception {
        if (bdSpecificationService.deleteById(id)) {
            return Result.success();
        }
        return Result.failed("删除失败");
    }

    @PutMapping(value = "/deleteByIds")
    public Result deleteBatch(@RequestBody String[] ids) {
        bdSpecificationService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable String id) throws Exception {
        BdSpecification result = bdSpecificationService.detail(id);
        return Result.success(result);
    }
}
