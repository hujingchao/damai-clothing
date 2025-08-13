package com.fenglei.admin.controller.system;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fenglei.common.constant.AdminConstants;
import com.fenglei.common.enums.QueryModeEnum;
import com.fenglei.common.result.Result;
import com.fenglei.common.result.ResultCode;
import com.fenglei.model.system.dto.DeptDTO;
import com.fenglei.model.system.entity.SysDept;
import com.fenglei.model.system.vo.DeptVO;
import com.fenglei.model.system.vo.TreeVO;
import com.fenglei.service.system.ISysDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Api(tags = "部门接口")
@RestController
@RequestMapping("/api.admin/v1/depts")
@Slf4j
public class DeptController {

    @Autowired
    private ISysDeptService iSysDeptService;

    @ApiOperation(value = "列表分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "部门名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "status", value = "部门状态", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "queryMode", value = "查询模式", paramType = "query", dataType = "QueryModeEnum")

    })
    @GetMapping
    public Result list(String queryMode,
                       Integer status,
                       String name,
                       String staffId) {

        LambdaQueryWrapper<SysDept> baseQuery = new LambdaQueryWrapper<SysDept>()
                .orderByAsc(SysDept::getSort)
                .orderByDesc(SysDept::getGmtModified)
                .orderByDesc(SysDept::getGmtCreate);
        QueryModeEnum queryModeEnum = QueryModeEnum.getValue(queryMode);

        switch (queryModeEnum) {
            case LIST:
                baseQuery = baseQuery
                        .like(StrUtil.isNotBlank(name), SysDept::getName, name)
                        .eq(status != null, SysDept::getStatus, status);
                List<DeptVO> list = iSysDeptService.listDeptVO(baseQuery, staffId);
                return Result.success(list);
            case TREE:
                List<TreeVO> treeList = iSysDeptService.listTreeVO(baseQuery);
                return Result.success(treeList);
            default:
                return Result.failed(ResultCode.QUERY_MODE_IS_NULL);
        }
    }

    @ApiOperation(value = "部门详情")
    @ApiImplicitParam(name = "id", value = "部门id", required = true, paramType = "path", dataType = "Long")
    @GetMapping("/{id}")
    public Result detail(@PathVariable String id) {

        SysDept sysDept = iSysDeptService.getById(id);
        System.out.println(sysDept);
        return Result.success(sysDept);
    }

    @ApiOperation(value = "新增部门")
    @ApiImplicitParam(name = "sysDept", value = "实体JSON对象", required = true, paramType = "body", dataType = "SysDept")
    @PostMapping
    public Result add(@RequestBody SysDept sysDept) {
        String treePath = getDeptTreePath(sysDept);
        sysDept.setTreePath(treePath);
        boolean status = iSysDeptService.save(sysDept);
        return Result.judge(status);
    }

    @ApiOperation(value = "修改部门")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "部门id", required = true, paramType = "path", dataType = "Long"),
            @ApiImplicitParam(name = "sysDept", value = "实体JSON对象", required = true, paramType = "body", dataType = "SysDept")
    })
    @PutMapping(value = "/{id}")
    public Result update(
            @PathVariable String id,
            @RequestBody SysDept sysDept) {

        String treePath = getDeptTreePath(sysDept);
        sysDept.setTreePath(treePath);
        //DY修改 这里考虑trf处理异常
        return Result.judge(iSysDeptService.updateById(sysDept));
    }


    public List<SysDept> treeMenuList(List<SysDept> deptList, String pid) {
        List<SysDept> childMenu = new ArrayList<>();
        for (SysDept mu : deptList) {
            //遍历出父id等于参数的id，add进子节点集合
            if (mu.getParentId().equals(pid)) {
                //递归遍历下一级
                treeMenuList(deptList, mu.getId());
                childMenu.add(mu);
            }
        }
        return childMenu;
    }


    @ApiOperation(value = "删除部门")
    @ApiImplicitParam(name = "ids", value = "id集合", required = true, paramType = "query", dataType = "String")
    @DeleteMapping("/{ids}")
    public Result delete(@PathVariable("ids") String ids) {
        AtomicBoolean result = new AtomicBoolean(true);
        List<String> idList = Arrays.asList(ids.split(","));
        // 删除部门以及子部门
        Optional.ofNullable(idList).orElse(new ArrayList<>()).forEach(id ->
                result.set(iSysDeptService.remove(new LambdaQueryWrapper<SysDept>().eq(SysDept::getId, id)
                        .or().apply("concat (',',tree_path,',') like concat('%,',{0},',%')", id)))
        );
        return Result.judge(result.get());
    }

    private String getDeptTreePath(SysDept sysDept) {
        String parentId = sysDept.getParentId();
        String treePath;
        if (parentId.equals(AdminConstants.ROOT_DEPT_ID)) {
            treePath = String.valueOf(AdminConstants.ROOT_DEPT_ID);
        } else {
            SysDept parentDept = iSysDeptService.getById(parentId);
            treePath = Optional.ofNullable(parentDept).map(dept -> dept.getTreePath() + "," + dept.getId()).orElse(Strings.EMPTY);
        }
        return treePath;
    }

    @GetMapping("getAll")
    public Result getAll() {
        List<SysDept> sysDeptList = iSysDeptService.list(new LambdaQueryWrapper<SysDept>()
                .eq(SysDept::getDeleted, 0)
        );
        return Result.success(sysDeptList);
    }


    @ApiOperation(value = "查询某一部门名字")
    @GetMapping("/getById/{id}")
    public Result getDeptById(@PathVariable String id) {
        DeptDTO deptDTO = iSysDeptService.getDeptById(id);
        return Result.success(deptDTO);
    }

    @GetMapping("/getByDeptName/{deptName}")
    public Result getByDeptName(@PathVariable String deptName) {
        List<SysDept> sysDepts = iSysDeptService.getByDeptName(deptName);
        return Result.success(sysDepts);
    }

    @ApiOperation(value = "根据ids查询")
    @PostMapping("/listByIds")
    public Result listByIds(@RequestBody Collection<? extends Serializable> ids) {
        List<SysDept> sysDeptList = iSysDeptService.listByIds(ids);
        return Result.success(sysDeptList);
    }

    @ApiOperation(value = "根据pids查询")
    @PostMapping("/listByPids")
    public Result listByPids(@RequestBody Collection<? extends Serializable> pids) {
        List<SysDept> sysDeptList = iSysDeptService.list(new QueryWrapper<SysDept>().lambda()
                .in(SysDept::getParentId, pids));
        return Result.success(sysDeptList);
    }

    @ApiOperation(value = "获取部门树结构")
    @GetMapping("/treeByDep")
    public Result treeByDep() {
        LambdaQueryWrapper<SysDept> baseQuery = new LambdaQueryWrapper<SysDept>()
                .orderByAsc(SysDept::getSort)
                .orderByDesc(SysDept::getGmtModified)
                .orderByDesc(SysDept::getGmtCreate);
        List<TreeVO> treeList = iSysDeptService.listTreeVO(baseQuery);
        return Result.success(treeList);
    }

    @GetMapping("/listTree")
    public Result listTree() {
        return Result.success(iSysDeptService.listTree());
    }
}
