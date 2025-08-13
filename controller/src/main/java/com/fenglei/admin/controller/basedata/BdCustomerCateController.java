package com.fenglei.admin.controller.basedata;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.result.Result;
import com.fenglei.model.basedata.BdCustomerCate;
import com.fenglei.service.basedata.BdCustomerCateService;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 客户分类模块Controller
 * Created by ljw on 2022/7/12.
 */
@RestController
@RequestMapping("/BdCustomerCate")
public class BdCustomerCateController {
    @Resource
    private BdCustomerCateService bdCustomerCateService;

    /**
     * 分页查询客户分类
     * @return
     */
    @GetMapping(value = "/page")
    public Result page(BdCustomerCate eqEquipmentCategory, Page page) {
        IPage<BdCustomerCate> bdCustomerCateIPage = bdCustomerCateService.myPage(eqEquipmentCategory,page);
        return Result.success(bdCustomerCateIPage);
    }

    @GetMapping(value = "/list")
    public Result list(BdCustomerCate bdCustomerCate) {
        List<BdCustomerCate> list = bdCustomerCateService.myList(bdCustomerCate);
        return Result.success(list);
    }

    /**
     * 添加产品分类
     * @param bdCustomerCate
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result create(@RequestBody BdCustomerCate bdCustomerCate) {
        BdCustomerCate result = bdCustomerCateService.add(bdCustomerCate);
        return Result.success(result);
    }

    /**
     * 修改客户分类
     * @param bdCustomerCate
     * @return
     */
    @ApiOperation(value = "修改客户分类", notes = "修改客户分类")
    @PutMapping("/update")
    public Result update(@RequestBody BdCustomerCate bdCustomerCate) {
        BdCustomerCate result = bdCustomerCateService.myUpdate(bdCustomerCate);
        return Result.success(result);
    }

    @ApiOperation(value = "删除客户分类", notes = "删除分类")
    @DeleteMapping("/del/{id}")
    public Result delete(@PathVariable Long id) throws Exception {
        if (bdCustomerCateService.deleteById(id)) {
            return Result.success();
        }
        return Result.failed("删除失败");
    }

    @PutMapping(value = "/deleteByIds")
    public Result delete(@RequestBody String[] ids) {
        bdCustomerCateService.deleteBatch(ids);
        return Result.success("删除成功");
    }

    /**
     * 根据id获取客户分类
     * @param id
     * @return
     */
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public Result<BdCustomerCate> detail(@PathVariable String id) {
        BdCustomerCate bdCustomerCate = bdCustomerCateService.detail(id);
        return Result.success(bdCustomerCate);
    }
}
