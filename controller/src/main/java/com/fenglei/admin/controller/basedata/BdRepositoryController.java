package com.fenglei.admin.controller.basedata;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.result.Result;
import com.fenglei.model.basedata.BdRepository;
import com.fenglei.service.basedata.BdRepositoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ljw
 */
@Api(tags = "仓库管理接口")
@RestController
@RequestMapping("/BdRepository")
@RequiredArgsConstructor
public class BdRepositoryController {

    @Resource
    private BdRepositoryService bdRepositoryService;

    @ApiOperation(value = "仓库分页", notes = "仓库分页")
    @GetMapping("/page")
    public Result page(Page page, BdRepository bdRepository) throws Exception {
        IPage iPage = bdRepositoryService.myPage(page, bdRepository);
        return Result.success(iPage);
    }

    @ApiOperation(value = "仓库列表", notes = "仓库列表")
    @GetMapping("/list")
    public Result list(BdRepository bdRepository) throws Exception {
        List<BdRepository> list = bdRepositoryService.myList(bdRepository);
        return Result.success(list);
    }

    @ApiOperation(value = "新增仓库", notes = "新增仓库")
    @PostMapping("/add")
    public Result add(@RequestBody BdRepository bdRepository) throws Exception {
        BdRepository result = bdRepositoryService.add(bdRepository);
        return Result.success(result);
    }

    @ApiOperation(value = "修改仓库", notes = "修改仓库")
    @PutMapping("/update")
    public Result update(@RequestBody BdRepository bdRepository) throws Exception {
        BdRepository result = bdRepositoryService.myUpdate(bdRepository);
        return Result.success(result);
    }

    @ApiOperation(value = "删除仓库", notes = "删除仓库")
    @DeleteMapping("/del/{id}")
    public Result delete(@PathVariable Long id) throws Exception {
        if (bdRepositoryService.deleteById(id)) {
            return Result.success();
        }
        return Result.failed("删除失败");
    }

    @PutMapping(value = "/deleteByIds")
    public Result deleteBatch(@RequestBody String[] ids) {
        bdRepositoryService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable String id) throws Exception {
        BdRepository result = bdRepositoryService.detail(id);
        return Result.success(result);
    }
}
