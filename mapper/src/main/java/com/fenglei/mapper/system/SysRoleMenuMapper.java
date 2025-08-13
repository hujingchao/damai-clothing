package com.fenglei.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenglei.model.system.entity.SysMenu;
import com.fenglei.model.system.entity.SysRoleMenu;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

    @Select("<script>" +
            "  select role_id from sys_role_menu where menu_id=#{menuId} " +
            "</script>")
    List<Integer> listByMenuId(Integer menuId);


    @Select(" SELECT " +
            " 	t1.menu_id  " +
            " FROM " +
            " 	sys_role_menu t1 " +
            " 	INNER JOIN sys_menu t2 ON t1.menu_id = t2.id  " +
            " WHERE role_id =#{roleId}")
    List<String> listMenuIds(String roleId);

    /**
     * 查询用户的所有菜单ID
     *
     * @param userId 用户id
     * @return 该用户所有可用的菜单
     */
    List<SysMenu> listMenuByUserId(String userId);


    /**
     * 根据用户id查询roleId
     * @param userId
     * @return
     */
    Integer selectRoleIdByUserId( @Param("userId")String userId);
}
