package com.fenglei.service.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.model.system.dto.DeptDTO;
import com.fenglei.model.system.entity.SysDept;
import com.fenglei.model.system.vo.DeptVO;
import com.fenglei.model.system.vo.TreeVO;

import java.util.List;

public interface ISysDeptService extends IService<SysDept> {

    List<DeptVO> listDeptVO(LambdaQueryWrapper<SysDept> baseQuery, String staffId);

    List<TreeVO> listTreeVO(LambdaQueryWrapper<SysDept> baseQuery);

    List<SysDept> getByDeptName(String deptName);

    List<DeptVO> listTree();

    DeptDTO getDeptById(String id);
}
