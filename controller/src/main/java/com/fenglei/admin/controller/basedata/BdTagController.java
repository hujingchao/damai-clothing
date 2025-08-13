package com.fenglei.admin.controller.basedata;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.result.Result;
import com.fenglei.model.basedata.BdTag;
import com.fenglei.service.basedata.BdTagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ljw
 */
@Api(tags = "标签管理接口")
@RestController
@RequestMapping("/BdTag")
@RequiredArgsConstructor
public class BdTagController {

    @Resource
    private BdTagService bdTagService;

    @ApiOperation(value = "标签分页", notes = "标签分页")
    @GetMapping("/page")
    public Result page(Page page, BdTag bdTag) throws Exception {
        IPage iPage = bdTagService.myPage(page, bdTag);
        return Result.success(iPage);
    }

    @ApiOperation(value = "标签列表", notes = "标签列表")
    @GetMapping("/list")
    public Result list(BdTag bdTag) throws Exception {
        List<BdTag> list = bdTagService.myList(bdTag);
        return Result.success(list);
    }

    @ApiOperation(value = "根据ids查询", notes = "根据ids查询")
    @PostMapping("/listByIds")
    public Result listByIds(@RequestBody List<String> ids) throws Exception {
        List<BdTag> bdTags = bdTagService.listByIds(ids);
        String remark =null;
        if(bdTags.size()>0){
            Set<String>  tagNames = bdTags.stream().map(BdTag::getName).collect(Collectors.toSet());
             remark = StringUtils.join(tagNames, ",");
        }
        return Result.success(remark);
    }

    @ApiOperation(value = "新增标签", notes = "新增标签")
    @PostMapping("/add")
    public Result add(@RequestBody BdTag bdTag) throws Exception {
        BdTag result = bdTagService.add(bdTag);
        return Result.success(result);
    }

    @ApiOperation(value = "修改标签", notes = "修改标签")
    @PutMapping("/update")
    public Result update(@RequestBody BdTag bdTag) throws Exception {
        BdTag result = bdTagService.myUpdate(bdTag);
        return Result.success(result);
    }

    @ApiOperation(value = "删除标签", notes = "删除标签")
    @DeleteMapping("/del/{id}")
    public Result delete(@PathVariable Long id) throws Exception {
        if (bdTagService.deleteById(id)) {
            return Result.success();
        }
        return Result.failed("删除失败");
    }

    @PutMapping(value = "/deleteByIds")
    public Result deleteBatch(@RequestBody String[] ids) {
        bdTagService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable String id) throws Exception {
        BdTag result = bdTagService.detail(id);
        return Result.success(result);
    }
}
