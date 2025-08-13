package com.fenglei.admin.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.result.Result;
import com.fenglei.model.system.entity.Approving;
import com.fenglei.service.system.ApprovingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ljw
 */
@Api(tags = "待审批管理接口")
@RestController
@RequestMapping("/Approving")
@RequiredArgsConstructor
public class ApprovingController {

    @Resource
    private ApprovingService approvingService;

    @ApiOperation(value = "待审批分页", notes = "待审批分页")
    @GetMapping("/page")
    public Result page(Page page, Approving approving) throws Exception {
        IPage<Approving> iPage = approvingService.myPage(page, approving);
        return Result.success(iPage);
    }

    @ApiOperation(value = "待审批列表", notes = "待审批列表")
    @GetMapping("/list")
    public Result list(Approving approving) throws Exception {
        List<Approving> list = approvingService.myList(approving);
        return Result.success(list);
    }
}
