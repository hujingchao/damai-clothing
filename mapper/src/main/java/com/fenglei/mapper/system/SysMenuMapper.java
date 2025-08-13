package com.fenglei.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenglei.model.system.entity.SysMenu;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author qj
 * @date 2020-11-06
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    @Select("<script>" +
            "   select id,name,parent_id,path,component,icon,sort,visible,redirect,title,keep_alive,is_title from sys_menu " +
            "   order by sort asc" +
            "</script>")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            // 一对多关联查询拥有菜单访问权限的角色ID集合
            @Result(property = "roles", column = "id", many = @Many(select = "com.fenglei.mapper.system.SysRoleMenuMapper.listByMenuId"))
    })
    List<SysMenu> listForRouter();

    /**
     * 查找crm父级id
     *
     * @param rootId
     * @return
     */
    @Select({
            "<script>",
            "   SELECT id FROM sys_menu WHERE parent_id = #{rootId} AND path LIKE '/crm%'",
            "</script>"
    })
    String getCrmParent(@Param("rootId") String rootId);
}
