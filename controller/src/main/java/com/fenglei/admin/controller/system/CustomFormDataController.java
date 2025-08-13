package com.fenglei.admin.controller.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.result.Result;
import com.fenglei.model.workFlow.dto.QueryDataForCustomFormData;
import com.fenglei.model.workFlow.entity.CustomFormData;
import com.fenglei.service.workFlow.CustomFormDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@Api(value="自定义表单管理接口")
@RestController
@RequestMapping("/api.admin/v1/flowable")
@AllArgsConstructor
@Slf4j
public class CustomFormDataController {

    @Resource
    private CustomFormDataService customFormDataService;

    @ApiOperation(value="保存自定义表单数据", notes="保存自定义表单数据", produces="application/json")
    @PostMapping("/customFormData")
    public Result saveCustomFormData(@RequestBody CustomFormData customFormData) {
        if (customFormDataService.saveOrUpdate(customFormData)) {
            return Result.success("保存成功");
        }
        return Result.failed("保存失败");
    }

    @ApiOperation(value="修改自定义表单数据", notes="修改自定义表单数据", produces="application/json")
    @PutMapping("/customFormData")
    public Result editCustomFormData(@RequestBody CustomFormData customFormData) {
        if (customFormDataService.saveOrUpdate(customFormData)) {
            return Result.success("修改成功");
        }
        return Result.failed("修改失败");
    }

    @ApiOperation(value="删除自定义表单数据", notes="删除自定义表单数据", produces="application/json")
    @DeleteMapping("/customFormData/{id}")
    public Result deleteCustomFormData(@PathVariable String id) {
        if (customFormDataService.removeById(id)) {
            return Result.success("删除成功");
        }
        return Result.failed("删除失败");
    }

    @ApiOperation(value="自定义表单数据列表", notes="自定义表单数据列表", produces="application/json")
    @GetMapping("/customFormData")
    public Result customFormPageData(Page<CustomFormData> page, CustomFormData customFormData, QueryDataForCustomFormData queryDataForCustomFormData) throws Exception {
        return Result.success( customFormDataService.myPage(page, customFormData, queryDataForCustomFormData));
    }

    @ApiOperation(value="获取单个自定义表单", notes="获取单个自定义表单", produces="application/json")
    @GetMapping("/customFormData/{id}")
    public Result getOneCustomFormData(@PathVariable String id) {
        CustomFormData customFormData = customFormDataService.getById(id);
        return Result.success(customFormData);
    }

}
