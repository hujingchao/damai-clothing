package com.fenglei.model.report.dto;

import lombok.Data;

@Data
public class CuttingTicketReportDto {

    //查询月份 yyyy-MM
    private String month;


    private String startDate;

    private String endDate;

    private String colorLike;

    private String specificationLike;

    private String productId;


    //物料类型 原材料 辅料
    private String selectType;

}
