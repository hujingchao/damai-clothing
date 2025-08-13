package com.fenglei.model.prd.entity;

import java.util.List;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.UBillItemBaseEntity;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 裁床单 - 工票信息
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-02
 */
@Getter
@Setter
@TableName("prd_cutting_ticket")
@ApiModel(value = "PrdCuttingTicket对象", description = "裁床单 - 工票信息")
public class PrdCuttingTicket extends UBillItemBaseEntity {

    @ApiModelProperty("skuId")
    private String skuId;

    @ApiModelProperty("颜色id")
    private String colorId;

    @ApiModelProperty("颜色")
    private String color;

    @ApiModelProperty("规格尺码id")
    private String specificationId;

    @ApiModelProperty("规格尺码")
    private String specification;

    @ApiModelProperty("总数量")
    private BigDecimal sumQty;

    @ApiModelProperty("票数")
    private Integer count;

    @ApiModelProperty("每票数量")
    private BigDecimal eachQty;

    @ApiModelProperty("完成数量（入库数量）")
    private BigDecimal finishQty;

    @ApiModelProperty("已汇报数量")
    private BigDecimal reportedQty;

    @ApiModelProperty("源单类型")
    private String srcType;

    @ApiModelProperty("源单id")
    private String srcId;

    @ApiModelProperty("源单编号")
    private String srcNo;

    @ApiModelProperty("源单itemId")
    private String srcItemId;

    @ApiModelProperty("源单总数")
    private BigDecimal srcAllQty;
    @ApiModelProperty("源单已裁剪数量")
    private BigDecimal srcCuttingQty;
    @ApiModelProperty("源单可裁剪数量")
    private BigDecimal srcQty;



    @ApiModelProperty("id集合，用于接收前台传参，如批量删除")
    @TableField(exist = false)
    private List<String> ids;


    @ApiModelProperty("裁床单 - 工票信息明细")
    @TableField(exist = false)
    private List<PrdCuttingTicketItem> cuttingTicketItems;

    @ApiModelProperty("未汇报数量")
    @TableField(exist = false)
    private BigDecimal unReportedQty;

    @ApiModelProperty("单位")
    @TableField(exist = false)
    private String unitName;
}
