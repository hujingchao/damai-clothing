package com.fenglei.mapper.prd;

import com.fenglei.model.prd.entity.PrdCuttingTicketItemReport;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 裁床单 - 工票信息明细汇报 Mapper 接口
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-02
 */
public interface PrdCuttingTicketItemReportMapper extends BaseMapper<PrdCuttingTicketItemReport> {

    List<PrdCuttingTicketItemReport> listByCuttingIds(@Param("cuttingIds") List<String> cuttingIds);

    List<PrdCuttingTicketItemReport> listByCuttingId(@Param("cuttingId") String cuttingId);

    List<PrdCuttingTicketItemReport> listGroupData(@Param("itemIds") List<String> itemIds);
}
