package com.fenglei.admin.controller.basedata;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.result.Result;
import com.fenglei.model.basedata.BdSupplier;
import com.fenglei.service.basedata.BdSupplierService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ljw
 */
@Api(tags = "供应商管理接口")
@RestController
@RequestMapping("/BdSupplier")
@RequiredArgsConstructor
public class BdSupplierController {

    @Resource
    private BdSupplierService bdSupplierService;

    @ApiOperation(value = "供应商分页", notes = "供应商分页")
    @GetMapping("/page")
    public Result page(Page page, BdSupplier bdSupplier) throws Exception {
        IPage iPage = bdSupplierService.myPage(page, bdSupplier);
        return Result.success(iPage);
    }

    @ApiOperation(value = "供应商列表", notes = "供应商列表")
    @GetMapping("/list")
    public Result list(BdSupplier bdSupplier) throws Exception {
        List<BdSupplier> list = bdSupplierService.myList(bdSupplier);
        return Result.success(list);
    }

    @ApiOperation(value = "新增供应商", notes = "新增供应商")
    @PostMapping("/add")
    public Result add(@RequestBody BdSupplier bdSupplier) throws Exception {
        BdSupplier result = bdSupplierService.add(bdSupplier);
        return Result.success(result);
    }

    @ApiOperation(value = "修改供应商", notes = "修改供应商")
    @PutMapping("/update")
    public Result update(@RequestBody BdSupplier bdSupplier) throws Exception {
        BdSupplier result = bdSupplierService.myUpdate(bdSupplier);
        return Result.success(result);
    }

    @ApiOperation(value = "删除供应商", notes = "删除供应商")
    @DeleteMapping("/del/{id}")
    public Result delete(@PathVariable Long id) throws Exception {
        if (bdSupplierService.deleteById(id)) {
            return Result.success();
        }
        return Result.failed("删除失败");
    }

    @PutMapping(value = "/deleteByIds")
    public Result deleteBatch(@RequestBody String[] ids) {
        bdSupplierService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable String id) throws Exception {
        BdSupplier result = bdSupplierService.detail(id);
        return Result.success(result);
    }
}
