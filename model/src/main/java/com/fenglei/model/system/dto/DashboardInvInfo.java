package com.fenglei.model.system.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 首页库存数据
 */
@Data
public class DashboardInvInfo {

    @ApiModelProperty("仓库编码")
    private String repositoryCode;
    @ApiModelProperty("仓库名称")
    private String repositoryName;
    @ApiModelProperty("产品编码")
    private String productSn;
    @ApiModelProperty("产品名称")
    private String productName;
    @ApiModelProperty("商家编码")
    private String sjNumber;
    @ApiModelProperty("颜色")
    private String color;
    @ApiModelProperty("规格")
    private String specification;
    @ApiModelProperty("数量")
    private BigDecimal qty;
    @ApiModelProperty("匹数")
    private BigDecimal piQty;
}
