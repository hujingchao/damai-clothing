package com.fenglei.admin.controller.system;

import com.fenglei.common.result.Result;
import com.fenglei.model.system.dto.DashboardInvInfo;
import com.fenglei.model.system.dto.DashboardMoInfo;
import com.fenglei.model.system.dto.DashboardProductionInfo;
import com.fenglei.service.system.DashboardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description 首页控制台
 * @author qj
 * @date 2021-03-08
 */
@Api(tags = "首页控制台")
@RestController
@RequestMapping("/api.admin/v1/dashboard")
@Slf4j
@AllArgsConstructor
public class DashboardController {

    DashboardService dashboardService;

    @ApiOperation(value = "获取首页生产信息数据")
    @GetMapping("/getProductionInfo")
    public Result<DashboardProductionInfo> getProductionInfo(){
        return Result.success(dashboardService.getProductionInfo());
    }

    @ApiOperation(value = "查询前十库存数据")
    @GetMapping("/listTop10InvInfo")
    public Result<List<DashboardInvInfo>> listTop10InvInfo(){
        return Result.success(dashboardService.listTop10InvInfo());
    }

    @ApiOperation(value = "查询生产订单量")
    @GetMapping("/listMoCount")
    public Result<DashboardMoInfo> listMoCount(String dateType) throws ParseException {
        return Result.success(dashboardService.listMoCount(dateType));
    }
}
