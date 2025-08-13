package com.fenglei.security.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenglei.model.system.entity.SysMenu;
import com.fenglei.model.system.entity.SysUser;
import com.fenglei.security.pojo.CustomerContactUserDetail;
import com.fenglei.security.pojo.InterceptorLogs;
import com.fenglei.security.pojo.WechatAuthDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Description 用户
 * @Author zhouyiqiu
 * @Date 2021/12/15 11:01
 * @Version 1.0
 */
public interface UserMapper extends BaseMapper<SysUser> {

    CustomerContactUserDetail loadCustomerContactByOpenId(@Param("openId") String openId);

    Boolean addCustomerContact(@Param("customerContact") CustomerContactUserDetail customerContact);

    CustomerContactUserDetail loadCustomerContactByPhone(@Param("phone") String phone);

    WechatAuthDTO getWechatInfoByOpenId(@Param("openId") String openId);

    Boolean updateCustomerContact(@Param("customerContact") CustomerContactUserDetail customerContact);

    int insertInterceptorLog(@Param("method") String method, @Param("requestUrl") String requestUrl, @Param("queryString") String queryString, @Param("creatorId") String creatorId);

    List<InterceptorLogs> selectInterceptorLogs();

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
}
