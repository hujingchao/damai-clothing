package com.fenglei.mapper.prd;

import com.fenglei.model.prd.entity.PrdCuttingRaw;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 裁床单 - 原材料信息 Mapper 接口
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-11
 */
public interface PrdCuttingRawMapper extends BaseMapper<PrdCuttingRaw> {

    List<PrdCuttingRaw> listByPid(@Param("pid") String pid);
}
