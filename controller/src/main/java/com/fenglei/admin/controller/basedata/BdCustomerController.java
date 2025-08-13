package com.fenglei.admin.controller.basedata;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.result.Result;
import com.fenglei.model.basedata.BdCustomer;
import com.fenglei.service.basedata.BdCustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ljw
 */
@Api(tags = "客户管理接口")
@RestController
@RequestMapping("/BdCustomer")
@RequiredArgsConstructor
public class BdCustomerController {

    @Resource
    private BdCustomerService bdCustomerService;

    @ApiOperation(value = "客户分页", notes = "客户分页")
    @GetMapping("/page")
    public Result page(Page page, BdCustomer bdCustomer) throws Exception {
        IPage iPage = bdCustomerService.myPage(page, bdCustomer);
        return Result.success(iPage);
    }

    @ApiOperation(value = "客户列表", notes = "客户列表")
    @GetMapping("/list")
    public Result list(BdCustomer bdCustomer) throws Exception {
        List<BdCustomer> list = bdCustomerService.myList(bdCustomer);
        return Result.success(list);
    }

    @ApiOperation(value = "新增客户", notes = "新增客户")
    @PostMapping("/add")
    public Result add(@RequestBody BdCustomer bdCustomer) throws Exception {
        BdCustomer result = bdCustomerService.add(bdCustomer);
        return Result.success(result);
    }

    @ApiOperation(value = "修改客户", notes = "修改客户")
    @PutMapping("/update")
    public Result update(@RequestBody BdCustomer bdCustomer) throws Exception {
        BdCustomer result = bdCustomerService.myUpdate(bdCustomer);
        return Result.success(result);
    }

    @ApiOperation(value = "删除客户", notes = "删除客户")
    @DeleteMapping("/del/{id}")
    public Result delete(@PathVariable Long id) throws Exception {
        if (bdCustomerService.deleteById(id)) {
            return Result.success();
        }
        return Result.failed("删除失败");
    }

    @PutMapping(value = "/deleteByIds")
    public Result deleteBatch(@RequestBody String[] ids) {
        bdCustomerService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable String id) throws Exception {
        BdCustomer result = bdCustomerService.detail(id);
        return Result.success(result);
    }
}
