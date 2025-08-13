package com.fenglei.service.system.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.common.constant.AdminConstants;
import com.fenglei.common.util.StringUtils;
import com.fenglei.mapper.system.SysDeptMapper;
import com.fenglei.model.system.dto.DeptDTO;
import com.fenglei.model.system.entity.SysDept;
import com.fenglei.model.system.vo.DeptVO;
import com.fenglei.model.system.vo.TreeVO;
import com.fenglei.service.system.ISysDeptService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements ISysDeptService {
    private SysDeptMapper sSysDeptMapper;

    @Override
    public List<DeptVO> listDeptVO(LambdaQueryWrapper<SysDept> baseQuery, String staffId) {
        if (StringUtils.isNotBlank(staffId) && !"-1".equals(staffId)) {
            //创建一个空数组用来接收dept
            List<SysDept> depts = new ArrayList<>();
            //先查出这个员工的所属部门
            List<String> deptTree = sSysDeptMapper.getdeptTreeByStaffId(staffId);// [“9,10,11”,"8,4"]
            deptTree.forEach(s -> {
                //创建一个对象用来接收
                SysDept sysDept = new SysDept();
                String[] getDepts = s.split(",");//[8,14]
                //取最后一位
                String deptId = getDepts[getDepts.length - 1];
                sysDept = this.getById(deptId);
                sysDept.setParentId("0");
                //添加进数组
                depts.add(sysDept);
            });

            //查出这个员工所属部门的子部门
            List<SysDept> deptList = sSysDeptMapper.getChildrenDept(deptTree);
            //添加进数组
            deptList.forEach(v -> {
                depts.add(v);
            });
//            List<SysDept> deptList = this.baseMapper.selectList(baseQuery);
            List<DeptVO> list = recursionForTree(AdminConstants.ROOT_DEPT_ID, depts);
            return list;
        } else {
            List<SysDept> deptList = this.baseMapper.selectList(baseQuery);
            List<DeptVO> list = recursionForTree(AdminConstants.ROOT_DEPT_ID, deptList);
            return list;
        }

    }

    @Override
    public List<TreeVO> listTreeVO(LambdaQueryWrapper<SysDept> baseQuery) {
        List<SysDept> deptList = this.baseMapper.selectList(baseQuery);
        List<TreeVO> list = recursionForTreeSelect(AdminConstants.ROOT_DEPT_ID, deptList);
        return list;
    }
    @Override
    public List<SysDept> getByDeptName(String deptName){
        List<SysDept> sysDepts=sSysDeptMapper.getByDeptName(deptName);
        return sysDepts;
    }

    /**
     * 递归生成部门表格数据
     *
     * @param parentId
     * @param deptList
     * @return
     */
    public static List<DeptVO> recursionForTree(String parentId, List<SysDept> deptList) {
        List<DeptVO> list = new ArrayList<>();
        Optional.ofNullable(deptList).orElse(new ArrayList<>())
                .stream()
                .filter(dept -> dept.getParentId().equals(parentId))
                .forEach(dept -> {
                    DeptVO deptVO = new DeptVO();
                    BeanUtil.copyProperties(dept, deptVO);
                    List<DeptVO> children = recursionForTree(dept.getId(), deptList);
                    deptVO.setChildren(children);
                    list.add(deptVO);
                });
        return list;
    }


    /**
     * 递归生成部门树形下拉数据
     *
     * @param parentId
     * @param deptList
     * @return
     */
    public static List<TreeVO> recursionForTreeSelect(String parentId, List<SysDept> deptList) {
        List<TreeVO> list = new ArrayList<>();
        Optional.ofNullable(deptList).orElse(new ArrayList<>())
                .stream()
                .filter(dept -> dept.getParentId().equals(parentId))
                .forEach(dept -> {
                    TreeVO treeVO = new TreeVO();
                    treeVO.setId(dept.getId());
                    treeVO.setLabel(dept.getName());
                    List<TreeVO> children = recursionForTreeSelect(dept.getId(), deptList);
                    treeVO.setChildren(children);
                    list.add(treeVO);
                });
        return list;
    }

    @Override
    public DeptDTO getDeptById(String id) {
        DeptDTO deptDTO = sSysDeptMapper.getDeptDTOById(id);
        return deptDTO;
    }

    @Override
    public List<DeptVO> listTree() {
        List<SysDept> depts = list();
        List<DeptVO> list = recursionForTree(AdminConstants.ROOT_DEPT_ID, depts);
        return list;
    }
}
