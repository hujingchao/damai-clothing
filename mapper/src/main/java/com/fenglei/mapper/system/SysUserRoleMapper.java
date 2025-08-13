package com.fenglei.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenglei.model.system.entity.SysUserRole;
import org.apache.ibatis.annotations.Param;

/**
 * @Description 用户角色mapper
 * @Author zhouyiqiu
 * @Date 2021/12/3 12:46
 * @Version 1.0
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
    Boolean deleteByUserId(@Param("userId") String userId);
}
