package com.fenglei.model.inv.vo;

import com.fenglei.model.base.pojo.ExcelColumn;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
@Data
public class InvPackageItemVo {
    @ApiModelProperty("单号")
    @ExcelColumn(value = "打包编号", col = 1)
    private String packNo;

    @ApiModelProperty("包装人")
    @ExcelColumn(value = "包装人", col = 2)
    private String packer;

    @ApiModelProperty("打包日期")
    @ExcelColumn(value = "打包日期", col = 3)
    private String packTime;


    @ApiModelProperty("单据状态")
    private String billStatus;

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "主表id")
    private String pid;

    @ApiModelProperty(value = "序号")
    private Integer seq;

    @ApiModelProperty("库存id")
    private String invId;

    @ApiModelProperty("物料详情id")
    private String materialDetailId;

    @ApiModelProperty("库存数")
    @ExcelColumn(value = "库存数", col = 8)
    private BigDecimal allQty;

    @ApiModelProperty("包装使用数量")
    @ExcelColumn(value = "打包数量", col = 9)
    private BigDecimal usedQty;

    @ApiModelProperty("每包数量")
    @ExcelColumn(value = "每包数量", col = 10)
    private BigDecimal perQty;

    @ApiModelProperty("包数")
    @ExcelColumn(value = "包数", col = 11)
    private Integer count;

    @ApiModelProperty("单价(元)")
    private BigDecimal price;

    @ApiModelProperty("仓库id")
    private String repositoryId;

    @ApiModelProperty("货位Id")
    private String positionId;

    @ApiModelProperty("id集合，用于接收前台传参，如批量删除")
    private List<String> ids;

    @ApiModelProperty("大货款号，id")
    private String productId;

    @ApiModelProperty("大货款号，编码")
    private String productNum;

    @ApiModelProperty("大货款号，名称")
    @ExcelColumn(value = "名称", col = 4)
    private String productName;

    @ApiModelProperty("主图")
    private String mainPic;

    @ApiModelProperty("颜色")
    @ExcelColumn(value = "颜色", col = 5)
    private String color;

    @ApiModelProperty(value = "规格")
    @ExcelColumn(value = "规格尺码", col = 6)
    private String specification;

    @ApiModelProperty(value = "单位id")
    private String unitId;

    @ApiModelProperty(value = "单位名称")
    @ExcelColumn(value = "单位", col = 7)
    private String unitName;

    @ApiModelProperty("仓库")
    @ExcelColumn(value = "仓库", col = 12)
    private String repositoryName;

    @ApiModelProperty("货位")
    @ExcelColumn(value = "货位", col = 13)
    private String positionName;




}
