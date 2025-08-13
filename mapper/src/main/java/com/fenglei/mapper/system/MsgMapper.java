package com.fenglei.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.model.system.entity.SysMsg;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 消息 Mapper 接口
 * </p>
 *
 * @author zhouyiqiu
 * @since 2022-04-13
 */
public interface MsgMapper extends BaseMapper<SysMsg> {
   IPage<SysMsg> list(Page page, @Param("msg") SysMsg msg);
}
