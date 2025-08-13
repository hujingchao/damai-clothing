package com.fenglei.mapper.prd;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenglei.model.prd.entity.PrdMoSecondaryProcess;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PrdMoSecondaryProcessMapper extends BaseMapper<PrdMoSecondaryProcess> {

    List<PrdMoSecondaryProcess> listByPid(@Param("pid") String pid);
}
