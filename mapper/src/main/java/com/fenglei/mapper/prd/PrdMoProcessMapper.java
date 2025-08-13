package com.fenglei.mapper.prd;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenglei.model.prd.entity.PrdMo;
import com.fenglei.model.prd.entity.PrdMoProcess;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface PrdMoProcessMapper extends BaseMapper<PrdMoProcess> {

    /**
     * 根据pid查找
     * @param pid 生产订单id
     * @return 生产订单工序列表
     */
    List<PrdMoProcess> listByPid(@Param("pid") String pid);

    List<PrdMoProcess> listByPids(@Param("pids") Set<String> pids);
}
