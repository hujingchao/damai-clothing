package com.fenglei.admin.controller.basedata;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.result.Result;
import com.fenglei.model.basedata.BdUnit;
import com.fenglei.service.basedata.BdUnitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ljw
 */
@Api(tags = "工序管理接口")
@RestController
@RequestMapping("/BdUnit")
@RequiredArgsConstructor
public class BdUnitController {

    @Resource
    private BdUnitService bdUnitService;

    @ApiOperation(value = "工序分页", notes = "工序分页")
    @GetMapping("/page")
    public Result page(Page page, BdUnit bdUnit) throws Exception {
        IPage iPage = bdUnitService.myPage(page, bdUnit);
        return Result.success(iPage);
    }

    @ApiOperation(value = "工序列表", notes = "工序列表")
    @GetMapping("/list")
    public Result list(BdUnit bdUnit) throws Exception {
        List<BdUnit> list = bdUnitService.myList(bdUnit);
        return Result.success(list);
    }

    @ApiOperation(value = "新增工序", notes = "新增工序")
    @PostMapping("/add")
    public Result add(@RequestBody BdUnit bdUnit) throws Exception {
        BdUnit result = bdUnitService.add(bdUnit);
        return Result.success(result);
    }

    @ApiOperation(value = "修改工序", notes = "修改工序")
    @PutMapping("/update")
    public Result update(@RequestBody BdUnit bdUnit) throws Exception {
        BdUnit result = bdUnitService.myUpdate(bdUnit);
        return Result.success(result);
    }

    @ApiOperation(value = "删除工序", notes = "删除工序")
    @DeleteMapping("/del/{id}")
    public Result delete(@PathVariable Long id) throws Exception {
        if (bdUnitService.deleteById(id)) {
            return Result.success();
        }
        return Result.failed("删除失败");
    }

    @PutMapping(value = "/deleteByIds")
    public Result deleteBatch(@RequestBody String[] ids) {
        bdUnitService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable String id) throws Exception {
        BdUnit result = bdUnitService.detail(id);
        return Result.success(result);
    }
}