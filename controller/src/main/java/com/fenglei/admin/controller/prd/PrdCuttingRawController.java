package com.fenglei.admin.controller.prd;

import com.fenglei.service.prd.IPrdCuttingRawService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 裁床单 - 原材料信息 前端控制器
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-11
 */
@Api(tags = "裁床单 - 原材料信息 前端控制器")
@RestController
@RequestMapping("/api.admin/v1/prdCuttingRaw")
@AllArgsConstructor
@Slf4j
public class PrdCuttingRawController {
    IPrdCuttingRawService prdCuttingRawService;
}
