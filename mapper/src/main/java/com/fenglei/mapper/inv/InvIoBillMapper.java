package com.fenglei.mapper.inv;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.model.inv.entity.InvIoBill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 物料收发流水账 Mapper 接口
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-22
 */
public interface InvIoBillMapper extends BaseMapper<InvIoBill> {

    IPage<InvIoBill> myPage(Page<InvIoBill> page, @Param("invIoBill") InvIoBill invIoBill);

    List<InvIoBill> myPage(@Param("invIoBill") InvIoBill invIoBill);
}
