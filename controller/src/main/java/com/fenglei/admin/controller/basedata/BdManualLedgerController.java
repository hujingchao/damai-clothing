package com.fenglei.admin.controller.basedata;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.result.Result;
import com.fenglei.model.basedata.BdManualLedger;
import com.fenglei.service.basedata.BdManualLedgerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ljw
 */
@Api(tags = "手工帐管理接口")
@RestController
@RequestMapping("/BdManualLedger")
@RequiredArgsConstructor
public class BdManualLedgerController {

    @Resource
    private BdManualLedgerService bdManualLedgerService;

    @ApiOperation(value = "手工帐分页", notes = "手工帐分页")
    @GetMapping("/page")
    public Result page(Page page, BdManualLedger bdManualLedger) throws Exception {
        IPage iPage = bdManualLedgerService.myPage(page, bdManualLedger);
        return Result.success(iPage);
    }

    @ApiOperation(value = "手工帐列表", notes = "手工帐列表")
    @GetMapping("/list")
    public Result list(BdManualLedger bdManualLedger) throws Exception {
        List<BdManualLedger> list = bdManualLedgerService.myList(bdManualLedger);
        return Result.success(list);
    }

    @ApiOperation(value = "新增手工帐", notes = "新增手工帐")
    @PostMapping("/add")
    public Result add(@RequestBody BdManualLedger bdManualLedger) throws Exception {
        BdManualLedger result = bdManualLedgerService.add(bdManualLedger);
        return Result.success(result);
    }

    @ApiOperation(value = "修改手工帐", notes = "修改手工帐")
    @PutMapping("/update")
    public Result update(@RequestBody BdManualLedger bdManualLedger) throws Exception {
        BdManualLedger result = bdManualLedgerService.myUpdate(bdManualLedger);
        return Result.success(result);
    }

    @ApiOperation(value = "删除手工帐", notes = "删除手工帐")
    @DeleteMapping("/del/{id}")
    public Result delete(@PathVariable Long id) throws Exception {
        if (bdManualLedgerService.deleteById(id)) {
            return Result.success();
        }
        return Result.failed("删除失败");
    }

    @PutMapping(value = "/deleteByIds")
    public Result deleteBatch(@RequestBody String[] ids) {
        bdManualLedgerService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable String id) throws Exception {
        BdManualLedger result = bdManualLedgerService.detail(id);
        return Result.success(result);
    }
}
