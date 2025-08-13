package com.fenglei.admin.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.result.Result;
import com.fenglei.common.util.StringUtils;
import com.fenglei.model.workFlow.entity.TableInfo;
import com.fenglei.model.workFlow.entity.TableInfoItem;
import com.fenglei.service.workFlow.ITableInfoItemService;
import com.fenglei.service.workFlow.ITableInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Api(value = "表单管理接口")
@RestController
@RequestMapping("/api.admin/v1/tableInfo")
@AllArgsConstructor
@Slf4j
public class TableInfoController {

    @Resource
    private ITableInfoService iTableInfoService;
    @Resource
    private ITableInfoItemService iTableInfoItemService;

    @ApiOperation(value = "分页查询表单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "分页对象", required = true, paramType = "body", dataType = "Page"),
            @ApiImplicitParam(name = "tableInfo", value = "实体JSON对象", required = true, paramType = "body", dataType = "TableInfo")
    })
    @GetMapping("/tableInfo")
    public Result list(Page page, TableInfo tableInfo) throws Exception {
        IPage<TableInfo> result = iTableInfoService.page(page, new LambdaQueryWrapper<TableInfo>()
          .eq(StringUtils.isNotEmpty(tableInfo.getTableCode()),TableInfo::getTableCode,tableInfo.getTableCode())
        );
        List<TableInfo> tableInfos = result.getRecords();
        List<String> ids = tableInfos.stream().map(TableInfo::getId).collect(Collectors.toList());
        if (ids.size() > 0) {
            List<TableInfoItem> tableInfoItems = iTableInfoItemService.list(new LambdaQueryWrapper<TableInfoItem>()
                    .in(TableInfoItem::getTableInfoId, ids.toArray())
                    .orderByDesc(TableInfoItem::getCreateTime));
            for (TableInfo info : tableInfos) {
                List<TableInfoItem> items = new ArrayList<>();
                for (TableInfoItem tableInfoItem : tableInfoItems) {
                    if (info.getId().equals(tableInfoItem.getTableInfoId())) {
                        items.add(tableInfoItem);
                    }
                }
                info.setTableInfoItems(items);
            }
            result.setRecords(tableInfos);
        }
        return Result.success(result);
    }

    @ApiOperation(value = "单个表单信息")
    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "path", dataType = "String")
    @GetMapping("/tableInfo/one/{id}")
    public Result get(@PathVariable String id) {
        return Result.success(iTableInfoService.findById(id));
    }

    @ApiOperation(value = "新增表单信息")
    @ApiImplicitParam(name = "tableInfo", value = "实体JSON对象", required = true, paramType = "body", dataType = "TableInfo")
    @PostMapping("/tableInfo")
    public Result add(@RequestBody TableInfo tableInfo) {
        int result = iTableInfoService.add(tableInfo);
        if (result >= 1) {
            return Result.success("保存成功");
        }
        return Result.failed("保存失败");
    }

    @ApiOperation(value = "更新表单信息")
    @ApiImplicitParam(name = "tableInfo", value = "实体JSON对象", required = true, paramType = "body", dataType = "TableInfo")
    @PutMapping("/tableInfo")
    public Result update(@RequestBody TableInfo tableInfo) {
        int result = iTableInfoService.update(tableInfo);
        if (result >= 1) {
            return Result.success("修改成功");
        }
        return Result.failed("更新失败");
    }

    @ApiOperation(value = "删除表单信息")
    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "path", dataType = "String")
    @DeleteMapping("/tableInfo/{id}")
    public Result delete(@PathVariable String id) {
        int result = iTableInfoService.delete(id);
        if (result >= 1) {
            return Result.success("删除成功");
        }
        return Result.failed("删除失败");
    }

    @ApiOperation(value = "单个表单信息")
    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "path", dataType = "String")
    @GetMapping("/tableInfo/order/list/{id}")
    public Result getOrderList(@PathVariable String id) {
        return Result.success(iTableInfoService.getOrderList(id));
    }

    @ApiOperation(value = "获取所有外部表单")
    @GetMapping("/tableInfo/order/list/getTableInfos")
    public Result getTableInfos() {
        return Result.success(iTableInfoService.getTableInfos());
    }

    @ApiOperation(value = "查询所有外部表单")
    @GetMapping("/tableInfo/all")
    public Result getTableInfoList() {
        List<TableInfo> tableInfos = iTableInfoService.list();
        return Result.success(tableInfos);
    }

    @ApiOperation(value = "删除外部表单")
    @ApiImplicitParam(name = "ids", value = "id集合", required = true, paramType = "query", dataType = "String")
    @DeleteMapping("/{ids}")
    public Result deleteByIds(@PathVariable String ids) {
        iTableInfoService.deleteBatch(ids);
        return Result.success("删除成功");
    }

    @GetMapping("/tableInfo/list")
    public Result getList(TableInfo tableInfo) {
        List<TableInfo> list = iTableInfoService.list(
                new QueryWrapper<TableInfo>().lambda()
                        .like(StringUtils.isNotEmpty(tableInfo.getTableName()), TableInfo::getTableName, tableInfo.getTableName())
                        .like(StringUtils.isNotEmpty(tableInfo.getFormName()), TableInfo::getFormName, tableInfo.getFormName())
        );
        return Result.success(list);
    }
}
