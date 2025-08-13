package com.fenglei.admin.controller.basedata;

import com.fenglei.common.result.Result;
import com.fenglei.model.basedata.BdPosition;
import com.fenglei.service.basedata.BdPositionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ljw
 */
@Api(tags = "货位管理接口")
@RestController
@RequestMapping("/BdPosition")
@RequiredArgsConstructor
public class BdPositionController {

    @Resource
    private BdPositionService bdPositionService;

    @ApiOperation(value = "货位列表", notes = "货位列表")
    @GetMapping("/list")
    public Result list(BdPosition bdPosition) throws Exception {
        List<BdPosition> list = bdPositionService.myList(bdPosition);
        return Result.success(list);
    }

}
