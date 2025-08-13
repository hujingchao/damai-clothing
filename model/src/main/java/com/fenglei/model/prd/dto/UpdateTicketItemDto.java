package com.fenglei.model.prd.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 改菲Dto
 */
@Data
public class UpdateTicketItemDto {
    /**
     * 裁床单工票信息的工票号详情id
     */
    private String cuttingTicketItemId;
    /**
     * 修改后的工票号数量
     */
    private BigDecimal qty;
}
