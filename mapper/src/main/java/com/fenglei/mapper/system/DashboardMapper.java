package com.fenglei.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenglei.model.system.dto.DashboardInvInfo;
import com.fenglei.model.system.dto.DashboardMoInfo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;


/**
 * @author yzy
 */

public interface DashboardMapper extends BaseMapper<Object> {
    /**
     * 获取今天完成数量
     */
    BigDecimal getTodayFinishedQty();

    /**
     * 获取在制量
     */
    BigDecimal getInProductionQty();

    /**
     * 获取今天工资
     */
    BigDecimal getTodaySalary();

    /**
     * 查询前十库存数据
     */
    List<DashboardInvInfo> listTop10InvInfo();

    /**
     * 查询生产订单量
     * @param beginDate 开始日期
     * @param endDate 结束日期
     */
    List<DashboardMoInfo> listMoCount(@Param("beginDate") String beginDate, @Param("endDate") String endDate);
}
