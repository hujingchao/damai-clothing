package com.fenglei.admin.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.result.Result;
import com.fenglei.common.util.StringUtils;
import com.fenglei.model.workFlow.entity.CustomForm;
import com.fenglei.service.workFlow.CustomFormService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@Api(value = "自定义表单接口")
@RestController
@RequestMapping("/api.admin/v1/flowable")
@AllArgsConstructor
@Slf4j
public class CustomFormController {

    @Resource
    private CustomFormService customFormService;

    @ApiOperation(value = "保存自定义表单", notes = "保存自定义表单", produces = "application/json")
    @PostMapping("/customForm")
    public Result saveCustomForm(@RequestBody CustomForm customForm) {
        List<CustomForm> customForms = customFormService.list(new QueryWrapper<CustomForm>().lambda().like(CustomForm::getTitle, customForm.getTitle()));
        if (customForms.size() > 0) {
            return Result.failed("该表单名称已存在");
        } else {
            if (customFormService.saveOrUpdate(customForm)) {
                return Result.success("保存成功");
            }
            return Result.failed("保存失败");
        }
    }

    @ApiOperation(value = "修改自定义表单", notes = "修改自定义表单", produces = "application/json")
    @PutMapping("/customForm")
    public Result editCustomForm(@RequestBody CustomForm customForm) {
        List<CustomForm> customForms = customFormService.list(new QueryWrapper<CustomForm>().lambda().like(CustomForm::getTitle, customForm.getTitle()));
        boolean flag = false;
        if (customForms.size() > 0) {
            if (customForms.size() == 1 && customForms.get(0).getId().equals(customForm.getId())) {
                flag = true;
            }
        } else {
            flag = true;
        }
        if (flag) {
            if (customFormService.saveOrUpdate(customForm)) {
                return Result.success("修改成功");
            }
        } else {
            return Result.failed("修改失败，该表单名称已存在");
        }
        return Result.failed("修改失败");
    }

    @ApiOperation(value = "删除自定义表单", notes = "删除自定义表单", produces = "application/json")
    @DeleteMapping("/customForm/{id}")
    public Result deleteCustomForm(@PathVariable String id) {
        if (customFormService.removeById(id)) {
            return Result.success("删除成功");
        }
        return Result.failed("删除失败");
    }

    @ApiOperation(value = "自定义表单列表", notes = "自定义表单列表", produces = "application/json")
    @GetMapping("/customForm")
    public Result customFormPage(Page<CustomForm> page, CustomForm customForm) {
        return Result.success(customFormService.page(page, new QueryWrapper<CustomForm>().lambda()
                .like(StringUtils.isNotEmpty(customForm.getTitle()), CustomForm::getTitle, customForm.getTitle())
                .eq(StringUtils.isNotNull(customForm.getStatus()), CustomForm::getStatus, customForm.getStatus())
                .orderByDesc(CustomForm::getCreateTime)
        ));
    }

    @ApiOperation(value = "获取单个自定义表单", notes = "获取单个自定义表单", produces = "application/json")
    @GetMapping("/customForm/{id}")
    public Result getOneCustomForm(@PathVariable String id) {
        CustomForm customForm = customFormService.getById(id);
        return Result.success(customForm);
    }

    @ApiOperation(value = "获取所有启用的自定义表单", notes = "获取所有启用的自定义表单", produces = "application/json")
    @GetMapping("/customForm/all")
    public Result getCustomFormList() {
        List<CustomForm> customForms = customFormService.list(new QueryWrapper<CustomForm>().lambda().eq(CustomForm::getStatus, true));
        return Result.success(customForms);
    }
}
