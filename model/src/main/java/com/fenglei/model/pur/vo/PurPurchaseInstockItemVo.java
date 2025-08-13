package com.fenglei.model.pur.vo;

import com.fenglei.model.base.pojo.ExcelColumn;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PurPurchaseInstockItemVo {

    @ExcelColumn(value = "单据编码", col = 1)
    @ApiModelProperty("单据编码")
    private String no;

    @ExcelColumn(value = "入库日期", col = 2)
    @ApiModelProperty("入库日期")
    private String bizDate;

    @ExcelColumn(value = "仓管员", col = 3)
    @ApiModelProperty("仓管员")
    private String stocker;

    @ExcelColumn(value = "采购订单", col = 4)
    @ApiModelProperty("采购订单")
    private String srcNo;

    @ExcelColumn(value = "入库类型", col = 5)
    @ApiModelProperty("入库类型")
    private String srcType;

    @ExcelColumn(value = "备注", col = 6)
    @ApiModelProperty("备注")
    private String remark;

    @ExcelColumn(value = "辅料编码", col = 11)
    @ApiModelProperty("辅料编码")
    private String number;

    @ExcelColumn(value = "辅料名称", col = 12)
    @ApiModelProperty("辅料名称")
    private String name;

    @ExcelColumn(value = "颜色/色号", col = 13)
    @ApiModelProperty("颜色/色号")
    private String color;


    @ExcelColumn(value = "规格", col = 14)
    @ApiModelProperty("规格")
    private String specification;


    @ExcelColumn(value = "单位", col = 15)
    @ApiModelProperty("单位")
    private String unitName;


    @ExcelColumn(value = "数量", col = 16)
    @ApiModelProperty("数量")
    private String qty;


    @ExcelColumn(value = "仓库", col = 17)
    @ApiModelProperty("仓库")
    private String repositoryName;


    @ExcelColumn(value = "货位", col = 18)
    @ApiModelProperty("货位")
    private String positionName;



    @ExcelColumn(value = "单价(元)", col = 19)
    @ApiModelProperty("单价(元)")
    private String price;

    @ExcelColumn(value = "匹数", col = 20)
    @ApiModelProperty("匹数")
    private String piQty;



    @ExcelColumn(value = "供应商", col = 21)
    @ApiModelProperty("供应商")
    private String supplierName;



    @ExcelColumn(value = "联系人", col = 22)
    @ApiModelProperty("联系人")
    private String contact;


    @ExcelColumn(value = "联系电话", col = 23)
    @ApiModelProperty("联系电话")
    private String contactNumber;


    @ExcelColumn(value = "地址", col = 24)
    @ApiModelProperty("地址")
    private String address;


    @ExcelColumn(value = "分录备注", col = 25)
    @ApiModelProperty("分录备注")
    private String itemRemark;

}
