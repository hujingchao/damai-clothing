package com.fenglei.model.prd.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
@Data
public class PrdInStockItemVo {

    @ApiModelProperty("单据id")
    private String inStockId;

    @ApiModelProperty("单据编号")
    private String inStockNo;

    @ApiModelProperty("收货人Id")
    private String receiveId;

    @ApiModelProperty("收货人")
    private String receiver;

    @ApiModelProperty("收货时间")
    private String receiveTime;

    @ApiModelProperty("备注信息")
    private String remark;

    private Integer billStatus;

    @ApiModelProperty("单据分录id")
    private String inStockItemId;

    @ApiModelProperty("置顶 默认0  置顶1")
    private Integer setTop;

    @ApiModelProperty("物料id")
    private String productId;
    @ApiModelProperty(value = "物料名称")
    private String productName;
    @ApiModelProperty(value = "物料编码")
    private String productNum;
    @ApiModelProperty("工票id")
    private String ticketItemId;
    @ApiModelProperty("工票号")
    private String ticketNo;
    @ApiModelProperty("收货数量(入库数量)")
    private String inStockNum;
    @ApiModelProperty("主图id")
    private String mainPicId;
    @ApiModelProperty("主图")
    private String mainPic;
    @ApiModelProperty("id集合，用于接收前台传参，如批量删除")
    private List<String> ids;

    @ApiModelProperty("关键词")
    private String keyWord;
    @ApiModelProperty("开始日期")
    private String beginDate;
    @ApiModelProperty("截止日期")
    private String endDate;
}
