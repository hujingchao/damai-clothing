package com.fenglei.security.mapper;

import com.fenglei.model.system.entity.SysUserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description 用户角色
 * @Author zhouyiqiu
 * @Date 2021/12/15 11:04
 * @Version 1.0
 */
public interface UserRoleMapper {

    List<SysUserRole> selectList(@Param("userId") String userId);
}
