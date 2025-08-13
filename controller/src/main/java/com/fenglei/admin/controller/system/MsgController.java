package com.fenglei.admin.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.result.Result;
import com.fenglei.model.system.entity.SysMsg;
import com.fenglei.service.system.IMsgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 消息 前端控制器
 * </p>
 *
 * @author zhouyiqiu
 * @since 2022-04-13
 */
@Api(value = "消息 前端控制器")
@RestController
@RequestMapping("/api.admin/v1/msg")
@AllArgsConstructor
@Slf4j
public class MsgController {
    IMsgService msgService;

    @ApiOperation(value = "消息新增")
    @PostMapping("/add")
    public Result add(@RequestBody SysMsg msg) {
        return Result.judge(msgService.add(msg));
    }

    @ApiOperation(value = "消息删除")
    @DeleteMapping("/removeById/{id}")
    public Result removeById(@PathVariable String id) {
        return Result.judge(msgService.myRemoveById(id));
    }

    @ApiOperation(value = "消息修改")
    @PutMapping("/update")
    public Result update(@RequestBody SysMsg msg) {
        return Result.judge(msgService.myUpdate(msg));
    }

    @ApiOperation(value = "消息分页查询")
    @GetMapping("/page")
    public Result page(Page page, SysMsg msg) {
        IPage<SysMsg> result = msgService.myPage(page, msg);
        return Result.success(result);
    }

    @ApiOperation(value = "消息详细信息")
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable String id) {
        SysMsg result = msgService.detail(id);
        return Result.success(result);
    }

    @ApiOperation(value = "设置已读")
    @PutMapping("/haveRead/{id}")
    public Result haveRead(@PathVariable("id") String id) {
        return Result.judge(msgService.haveRead(id));
    }

    @ApiOperation(value = "设置未读")
    @PutMapping("/unRead/{id}")
    public Result unRead(@PathVariable("id") String id) {
        return Result.judge(msgService.unRead(id));
    }

    @ApiOperation(value = "设置全部已读")
    @PutMapping("/allHaveRead")
    public Result allHaveRead() {
        return Result.judge(msgService.allHaveRead());
    }

    @ApiOperation(value = "获取未读的数量")
    @GetMapping("/unreadNumber")
    public Result unreadNumber() {
        return Result.success(msgService.unreadNumber());
    }
}
