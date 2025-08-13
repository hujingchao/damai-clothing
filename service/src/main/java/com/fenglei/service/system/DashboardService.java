package com.fenglei.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.model.system.dto.DashboardInvInfo;
import com.fenglei.model.system.dto.DashboardMoInfo;
import com.fenglei.model.system.dto.DashboardProductionInfo;

import java.text.ParseException;
import java.util.List;

public interface DashboardService extends IService<Object> {
    /**
     * 获取生产信息
     */
    DashboardProductionInfo getProductionInfo();

    /**
     * 查询前十库存数据
     */
    List<DashboardInvInfo> listTop10InvInfo();

    /**
     * 查询生产订单量
     */
    DashboardMoInfo listMoCount(String dateType) throws ParseException;
}
