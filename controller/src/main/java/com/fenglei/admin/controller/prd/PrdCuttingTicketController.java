package com.fenglei.admin.controller.prd;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.result.Result;
import com.fenglei.model.prd.entity.PrdCuttingTicket;
import com.fenglei.model.prd.vo.CuttingRouteDetailVo;
import com.fenglei.model.prd.vo.CuttingTicketItemDetailVo;
import com.fenglei.service.prd.IPrdCuttingTicketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 裁床单 - 工票信息 前端控制器
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-02
 */
@Api(tags = "裁床单 - 工票信息 前端控制器")
@RestController
@RequestMapping("/api.admin/v1/prdCuttingTicket")
@AllArgsConstructor
@Slf4j
public class PrdCuttingTicketController {
    IPrdCuttingTicketService prdCuttingTicketService;

    @ApiOperation(value = "根据裁床单工序id获取扎号详情列表")
    @GetMapping("/listRouteDetailByCuttingRouteId/{cuttingRouteId}")
    public Result<List<CuttingRouteDetailVo>> listRouteDetailByCuttingRouteId(@PathVariable String cuttingRouteId) {
        List<CuttingRouteDetailVo> result = prdCuttingTicketService.listRouteDetailByCuttingRouteId(cuttingRouteId);
        return Result.success(result);
    }
}
