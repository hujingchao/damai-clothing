package com.fenglei.service.prd.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fenglei.common.util.StringUtils;
import com.fenglei.model.prd.constants.BillNoConstants;
import com.fenglei.model.prd.entity.PrdCuttingTicketItemZ;
import com.fenglei.mapper.prd.PrdCuttingTicketItemZMapper;
import com.fenglei.service.prd.IPrdCuttingTicketItemZService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * <p>
 * 裁床单票号种子表 服务实现类
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-12
 */
@Service
public class PrdCuttingTicketItemZServiceImpl extends ServiceImpl<PrdCuttingTicketItemZMapper, PrdCuttingTicketItemZ> implements IPrdCuttingTicketItemZService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String getNo(String no) {
        if (StringUtils.isEmpty(no)) {
            Long num = 0L;
            PrdCuttingTicketItemZ cuttingTicketItemZ = new PrdCuttingTicketItemZ();
            cuttingTicketItemZ.setText("1");
            if (this.save(cuttingTicketItemZ)) {
                num = cuttingTicketItemZ.getId();
            }
            this.remove(Wrappers.lambdaQuery(PrdCuttingTicketItemZ.class).ne(PrdCuttingTicketItemZ::getId, cuttingTicketItemZ.getId()));
            return BillNoConstants.TICKET_NO_PREFIX + String.format("%0" + BillNoConstants.TICKET_NO_LENGTH + "d", num);
        } else {
            return no;
        }
    }
}
