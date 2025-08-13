package com.fenglei.mapper.oms;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.model.oms.entity.OmsSaleOutStock;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenglei.model.oms.entity.dto.OmsSaleOutStockDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 销售出库单 Mapper 接口
 * </p>
 *
 * @author zjn
 * @since 2024-04-15
 */
public interface OmsSaleOutStockMapper extends BaseMapper<OmsSaleOutStock> {

    IPage<OmsSaleOutStockDTO> getOutStockSummary(Page page,@Param("omsSaleOutStockDTO") OmsSaleOutStockDTO omsSaleOutStockDTO);

    List<OmsSaleOutStockDTO> getOutStockSummary(@Param("omsSaleOutStockDTO") OmsSaleOutStockDTO omsSaleOutStockDTO);
}
