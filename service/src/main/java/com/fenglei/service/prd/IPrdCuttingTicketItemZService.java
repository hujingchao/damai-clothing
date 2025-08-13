package com.fenglei.service.prd;

import com.fenglei.model.prd.entity.PrdCuttingTicketItemZ;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;

/**
 * <p>
 * 裁床单票号种子表 服务类
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-12
 */
public interface IPrdCuttingTicketItemZService extends IService<PrdCuttingTicketItemZ> {

    /**
     * 生产编号
     * @return
     */
    String getNo(String no);
}
