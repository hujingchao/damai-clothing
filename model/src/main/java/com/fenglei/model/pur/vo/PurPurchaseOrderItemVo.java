package com.fenglei.model.pur.vo;

import com.fenglei.model.base.pojo.ExcelColumn;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PurPurchaseOrderItemVo {

    @ExcelColumn(value = "单据编码", col = 1)
    @ApiModelProperty("单据编码")
    private String no;

    @ExcelColumn(value = "采购日期", col = 2)
    @ApiModelProperty("采购日期")
    private String bizDate;

    @ExcelColumn(value = "采购员", col = 3)
    @ApiModelProperty("采购员")
    private String purchaser;

    @ExcelColumn(value = "跟单员", col = 4)
    @ApiModelProperty("跟单员")
    private String follower;

    @ExcelColumn(value = "采购类型", col = 5)
    @ApiModelProperty("采购类型")
    private String billType;

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


    @ExcelColumn(value = "供应商", col = 17)
    @ApiModelProperty("供应商")
    private String supplierName;



    @ExcelColumn(value = "联系人", col = 18)
    @ApiModelProperty("联系人")
    private String contact;


    @ExcelColumn(value = "联系电话", col = 19)
    @ApiModelProperty("联系电话")
    private String contactNumber;


    @ExcelColumn(value = "地址", col = 20)
    @ApiModelProperty("地址")
    private String address;


    @ExcelColumn(value = "分录备注", col = 21)
    @ApiModelProperty("分录备注")
    private String itemRemark;

}
