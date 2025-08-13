package com.fenglei.model.prd.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
@Data
public class PrdPackingItemVo {

    @ApiModelProperty("单据id")
    private String packId;

    @ApiModelProperty("单据编号")
    private String packNo;

    @ApiModelProperty("包装人Id")
    private String packerId;

    @ApiModelProperty("包装人")
    private String packer;

    @ApiModelProperty("包装时间")
    private String packTime;

    @ApiModelProperty("备注信息")
    private String remark;

    private Integer billStatus;

    @ApiModelProperty("单据分录id")
    private String packItemId;

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
    @ApiModelProperty("包装数量")
    private String packNum;

    @ApiModelProperty("主图id")
    private String mainPicId;

    @ApiModelProperty("主图")
    private String mainPic;




    @ApiModelProperty("id集合，用于接收前台传参，如批量删除")
    private List<String> ids;

    @ApiModelProperty("关键词")
    private String keyWord;
}
