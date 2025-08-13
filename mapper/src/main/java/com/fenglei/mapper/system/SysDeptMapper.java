package com.fenglei.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenglei.model.system.dto.DeptDTO;
import com.fenglei.model.system.entity.SysDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface SysDeptMapper extends BaseMapper<SysDept> {
    List<String> getdeptTreeByStaffId(@Param("staffId") String staffId);

    List<SysDept> getChildrenDept(@Param("treePaths") List<String> treePaths);

    List<SysDept> getByDeptName(@Param("deptName")String deptName);

    List<SysDept> filterDept(@Param("staffId") String staffId);
    List<SysDept> getDeptByTree(@Param("ids") String[] ids);
    DeptDTO getDeptDTOById(String id);
}
