package com.fenglei.admin.controller.prd;

import com.fenglei.common.result.Result;
import com.fenglei.model.prd.entity.PrdCuttingTicketItem;
import com.fenglei.model.prd.vo.CuttingTicketItemDetailVo;
import com.fenglei.service.prd.IPrdCuttingTicketItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 裁床单 - 工票信息明细 前端控制器
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-02
 */
@Api(tags = "裁床单 - 工票信息明细 前端控制器")
@RestController
@RequestMapping("/api.admin/v1/prdCuttingTicketItem")
@AllArgsConstructor
@Slf4j
public class PrdCuttingTicketItemController {
    IPrdCuttingTicketItemService prdCuttingTicketItemService;

    @ApiOperation(value = "裁床单 - 工票信息明细详细信息")
    @GetMapping("/detail/{id}")
    public Result<PrdCuttingTicketItem> detail(@PathVariable String id) {
        PrdCuttingTicketItem result = prdCuttingTicketItemService.detail(id);
        return Result.success(result);
    }
}
