package com.fenglei.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.model.system.entity.SysUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Description
 * @Author zhouyiqiu
 * @Date 2021/12/2 13:43
 * @Version 1.0
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
    SysUser getUserByName(@Param("username") String username);

    @Select("<script> " +
            "   select u.* , GROUP_CONCAT(r.name) as roleNames" +
            "   from sys_user u " +
            "       left join sys_user_role ur on u.id=ur.user_id " +
            "       left join sys_role r on ur.role_id=r.id " +
            "   where u.deleted != 1 " +
            " <if test ='user.username!=null and user.username.trim() neq \"\"'>" +
            "       and u.username like concat('%',#{user.username},'%')" +
            " </if>" +
            " <if test ='user.nickname!=null and user.nickname.trim() neq \"\"'>" +
            "       and u.nickname like concat('%',#{user.nickname},'%')" +
            " </if>" +
            " <if test ='user.mobile!=null and user.mobile.trim() neq \"\"'>" +
            "       and u.mobile like concat('%',#{user.mobile},'%')" +
            " </if>" +
            " <if test ='user.status!=null and user.status>=0'>" +
            "       and u.status = #{user.status}" +
            " </if>" +
            " GROUP BY u.id " +
            "</script>")

    @Results({
            @Result(id = true, column = "id", property = "id")
    })
    List<SysUser> list(Page<SysUser> page, SysUser user);
}

