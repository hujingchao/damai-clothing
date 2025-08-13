package com.fenglei.model.report.dto;

import lombok.Data;

@Data
public class MaterialsReportDto {

    //查询月份 yyyy-MM
    private String month;

    //物料类型 原材料 辅料
    private String selectType;

}
