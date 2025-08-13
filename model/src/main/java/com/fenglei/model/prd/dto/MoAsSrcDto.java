package com.fenglei.model.prd.dto;

import com.fenglei.model.prd.entity.PrdCuttingRaw;
import com.fenglei.model.prd.entity.PrdCuttingRoute;
import com.fenglei.model.prd.entity.PrdCuttingTicket;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 生产订单作为源单的Dto
 */
@Data
public class MoAsSrcDto {

    @ApiModelProperty("内码")
    private String id;
    @ApiModelProperty("编号")
    private String no;

    @ApiModelProperty("单据类型")
    private String billType;

    @ApiModelProperty("发货日期")
    private String deliveryDate;
    @ApiModelProperty("大货款号，id")
    private String productId;
    @ApiModelProperty("大货款号，编码")
    private String productNum;
    @ApiModelProperty("大货款号，名称")
    private String productName;
    @ApiModelProperty("主图")
    private String mainPic;
    @ApiModelProperty("单位id")
    private String unitId;
    @ApiModelProperty("单位名称")
    private String unitName;


    @ApiModelProperty("工序信息")
    private List<PrdCuttingRoute> cuttingRoutes;
    @ApiModelProperty("工票信息")
    private List<PrdCuttingTicket> cuttingTickets;
    @ApiModelProperty("原材料信息")
    private List<PrdCuttingRaw> cuttingRaws;
}
