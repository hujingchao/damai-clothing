package com.fenglei.service.system;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.model.system.entity.SysMenu;
import com.fenglei.model.system.vo.MenuVO;
import com.fenglei.model.system.vo.RouterVO;
import com.fenglei.model.system.vo.TreeVO;

import java.util.List;

/**
 * @author qj
 * @date 2020-11-06
 */
public interface ISysMenuService extends IService<SysMenu> {

    List<MenuVO> listMenuVO(LambdaQueryWrapper<SysMenu> baseQuery);

    List<TreeVO> listTreeVO(LambdaQueryWrapper<SysMenu> baseQuery);

    List<RouterVO> listRouterVO();

    /**
     * 获取用户菜单列表
     *
     * @param userId 用户id
     * @return 菜单列表
     */
    List<SysMenu> listMenuByUserId(String userId);
}
