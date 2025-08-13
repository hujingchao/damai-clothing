package com.fenglei.admin.controller.basedata;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.result.Result;
import com.fenglei.model.basedata.BdProcedure;
import com.fenglei.service.basedata.BdProcedureService;
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
@RequestMapping("/BdProcedure")
@RequiredArgsConstructor
public class BdProcedureController {

    @Resource
    private BdProcedureService bdProcedureService;

    @ApiOperation(value = "工序分页", notes = "工序分页")
    @GetMapping("/page")
    public Result page(Page page, BdProcedure bdProcedure) throws Exception {
        IPage iPage = bdProcedureService.myPage(page, bdProcedure);
        return Result.success(iPage);
    }

    @ApiOperation(value = "工序列表", notes = "工序列表")
    @GetMapping("/list")
    public Result list(BdProcedure bdProcedure) throws Exception {
        List<BdProcedure> list = bdProcedureService.myList(bdProcedure);
        return Result.success(list);
    }

    @ApiOperation(value = "新增工序", notes = "新增工序")
    @PostMapping("/add")
    public Result add(@RequestBody BdProcedure bdProcedure) throws Exception {
        BdProcedure result = bdProcedureService.add(bdProcedure);
        return Result.success(result);
    }

    @ApiOperation(value = "修改工序", notes = "修改工序")
    @PutMapping("/update")
    public Result update(@RequestBody BdProcedure bdProcedure) throws Exception {
        BdProcedure result = bdProcedureService.myUpdate(bdProcedure);
        return Result.success(result);
    }

    @ApiOperation(value = "删除工序", notes = "删除工序")
    @DeleteMapping("/del/{id}")
    public Result delete(@PathVariable Long id) throws Exception {
        if (bdProcedureService.deleteById(id)) {
            return Result.success();
        }
        return Result.failed("删除失败");
    }

    @PutMapping(value = "/deleteByIds")
    public Result deleteBatch(@RequestBody String[] ids) {
        bdProcedureService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable String id) throws Exception {
        BdProcedure result = bdProcedureService.detail(id);
        return Result.success(result);
    }
}