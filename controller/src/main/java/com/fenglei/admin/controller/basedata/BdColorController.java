package com.fenglei.admin.controller.basedata;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.result.Result;
import com.fenglei.model.basedata.BdColor;
import com.fenglei.service.basedata.BdColorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ljw
 */
@Api(tags = "颜色/色号管理接口")
@RestController
@RequestMapping("/BdColor")
@RequiredArgsConstructor
public class BdColorController {

    @Resource
    private BdColorService bdColorService;

    @ApiOperation(value = "颜色/色号分页", notes = "颜色/色号分页")
    @GetMapping("/page")
    public Result page(Page page, BdColor bdColor) throws Exception {
        IPage iPage = bdColorService.myPage(page, bdColor);
        return Result.success(iPage);
    }

    @ApiOperation(value = "颜色/色号列表", notes = "颜色/色号列表")
    @GetMapping("/list")
    public Result list(BdColor bdColor) throws Exception {
        List<BdColor> list = bdColorService.myList(bdColor);
        return Result.success(list);
    }

    @ApiOperation(value = "新增颜色/色号", notes = "新增颜色/色号")
    @PostMapping("/add")
    public Result add(@RequestBody BdColor bdColor) throws Exception {
        BdColor result = bdColorService.add(bdColor);
        return Result.success(result);
    }

    @ApiOperation(value = "修改颜色/色号", notes = "修改颜色/色号")
    @PutMapping("/update")
    public Result update(@RequestBody BdColor bdColor) throws Exception {
        BdColor result = bdColorService.myUpdate(bdColor);
        return Result.success(result);
    }

    @ApiOperation(value = "删除颜色/色号", notes = "删除颜色/色号")
    @DeleteMapping("/del/{id}")
    public Result delete(@PathVariable Long id) throws Exception {
        if (bdColorService.deleteById(id)) {
            return Result.success();
        }
        return Result.failed("删除失败");
    }

    @PutMapping(value = "/deleteByIds")
    public Result deleteBatch(@RequestBody String[] ids) {
        bdColorService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable String id) throws Exception {
        BdColor result = bdColorService.detail(id);
        return Result.success(result);
    }
}
