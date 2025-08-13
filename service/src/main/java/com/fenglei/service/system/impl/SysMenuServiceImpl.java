package com.fenglei.service.system.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.common.constant.AdminConstants;
import com.fenglei.common.constant.GlobalConstants;
import com.fenglei.mapper.system.SysMenuMapper;
import com.fenglei.mapper.system.SysRoleMenuMapper;
import com.fenglei.model.system.entity.SysMenu;
import com.fenglei.model.system.vo.MenuVO;
import com.fenglei.model.system.vo.RouterVO;
import com.fenglei.model.system.vo.TreeVO;
import com.fenglei.service.system.ISysMenuService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * @Author qj
 * @Date 2020-11-06
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Resource
    private SysRoleMenuMapper sysRoleMenuMapper;


    @Override
    public List<MenuVO> listMenuVO(LambdaQueryWrapper<SysMenu> baseQuery) {
        List<SysMenu> menuList = this.baseMapper.selectList(baseQuery);
        List<MenuVO> list = recursionForTree(AdminConstants.ROOT_MENU_ID, menuList);
        return list;
    }

    @Override
    public List<TreeVO> listTreeVO(LambdaQueryWrapper<SysMenu> baseQuery) {
        List<SysMenu> menuList = this.list(baseQuery);
        List<TreeVO> list = recursionForTreeSelect(AdminConstants.ROOT_MENU_ID, menuList);
        return list;
    }

    @Override
    public List<RouterVO> listRouterVO() {
        List<SysMenu> menuList = this.baseMapper.listForRouter();
        List<RouterVO> list = recursionForRoutes(AdminConstants.ROOT_MENU_ID, menuList);
        return list;
    }

    // 递归生成路由
    private List<RouterVO> recursionForRoutes(String parentId, List<SysMenu> menuList) {
        List<RouterVO> list = new ArrayList<>();
        Optional.ofNullable(menuList).ifPresent(menus -> menus.stream().filter(menu -> menu.getParentId().equals(parentId))
                .forEach(menu -> {
                    RouterVO routerVO = new RouterVO();
                    routerVO.setName(menu.getName());
                    if (AdminConstants.ROOT_MENU_ID.equals(parentId)) {
                        routerVO.setPath(menu.getPath());
                        routerVO.setComponent("Layout");
                    } else {
                        routerVO.setPath(menu.getPath());// 显示在地址栏上
                        routerVO.setComponent(menu.getComponent());
                    }
                    routerVO.setRedirect(menu.getRedirect());
                    routerVO.setMeta(routerVO.new Meta(
                            menu.getTitle(),
                            menu.getKeepAlive(),
                            menu.getIsTitle(),
                            menu.getIcon(),
                            menu.getRoles()
                    ));
                    // 菜单显示隐藏
                    routerVO.setHidden(!GlobalConstants.VISIBLE_SHOW_VALUE.equals(menu.getVisible()) ? true : false);
                    List<RouterVO> children = recursionForRoutes(menu.getId(), menuList);
                    routerVO.setChildren(children);
                    if (CollectionUtil.isNotEmpty(children)) {
                        routerVO.setAlwaysShow(Boolean.TRUE); // 显示子节点
                    }
                    list.add(routerVO);
                }));
        return list;
    }

    /**
     * 递归生成部门表格数据
     *
     * @param parentId
     * @param menuList
     * @return
     */
    public static List<MenuVO> recursionForTree(String parentId, List<SysMenu> menuList) {
        List<MenuVO> list = new ArrayList<>();
        Optional.ofNullable(menuList).orElse(new ArrayList<>())
                .stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .forEach(menu -> {
                    MenuVO menuVO = new MenuVO();
                    BeanUtils.copyProperties(menu, menuVO);
                    List<MenuVO> children = recursionForTree(menu.getId(), menuList);
                    if (CollectionUtil.isNotEmpty(children)) {
                        menuVO.setChildren(children);
                    }
                    list.add(menuVO);
                });
        return list;
    }


    /**
     * 递归生成部门树形下拉数据
     *
     * @param parentId
     * @param menuList
     * @return
     */
    public static List<TreeVO> recursionForTreeSelect(String parentId, List<SysMenu> menuList) {
        List<TreeVO> list = new ArrayList<>();
        Optional.ofNullable(menuList).orElse(new ArrayList<>())
                .stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .forEach(menu -> {
                    TreeVO treeVO = new TreeVO();
                    treeVO.setId(menu.getId());
                    treeVO.setLabel(menu.getTitle());
                    List<TreeVO> children = recursionForTreeSelect(menu.getId(), menuList);
                    if (CollectionUtil.isNotEmpty(children)) {
                        treeVO.setChildren(children);
                    }
                    list.add(treeVO);
                });
        return list;
    }


    @Override
    public List<SysMenu> listMenuByUserId(String userId) {
        // 用户的所有菜单信息
        List<SysMenu> sysMenus;
        //系统管理员，拥有最高权限
        if (GlobalConstants.SUPER_ADMIN_ID.equals(userId)) {
            sysMenus = this.baseMapper.selectList(new QueryWrapper<SysMenu>().eq("visible", 1).orderByAsc("sort"));
        } else {
            sysMenus = sysRoleMenuMapper.listMenuByUserId(userId);
        }


        // 递归寻找下级菜单
        List<SysMenu> rootMenu = getMenuTreeList(sysMenus, "0");

        return rootMenu;
    }


    /**
     * 查询下级菜单
     *
     * @param menuList
     * @param pid
     * @return
     */
    private List<SysMenu> getMenuTreeList(List<SysMenu> menuList, String pid) {
        // 查找所有菜单
        List<SysMenu> childrenList = new ArrayList<>();
        menuList.stream()
                .filter(d -> Objects.equals(pid, d.getParentId()))
                .collect(Collectors.toList())
                .forEach(d -> {
                    //if(d.getMenuLeafNode().equals("0"))
                    d.setChildren(getMenuTreeList(menuList, d.getId()));
                    childrenList.add(d);
                });
        return childrenList.size() > 0 ? childrenList : null;
    }
}
