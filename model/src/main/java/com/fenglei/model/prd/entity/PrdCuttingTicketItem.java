package com.fenglei.model.prd.entity;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * 裁床单 - 工票信息明细
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-02
 */
@Getter
@Setter
@TableName("prd_cutting_ticket_item")
@ApiModel(value = "PrdCuttingTicketItem对象", description = "裁床单 - 工票信息明细")
public class PrdCuttingTicketItem implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "内码")
    @TableId(type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "主表id")
    private String pid;

    @ApiModelProperty(value = "序号")
    private Integer seq;

    @TableField(exist = false)
    @ApiModelProperty(value = "批量id")
    private List<String> ids;
    @TableField(exist = false)
    @ApiModelProperty(value = "批量主表id")
    private List<String> pids;

    @ApiModelProperty("裁床单id")
    private String gpId;

    @ApiModelProperty("工票号")
    private String ticketNo;

    @ApiModelProperty("数量")
    private BigDecimal qty;

    @ApiModelProperty("上数数量（汇报数量）")
    private BigDecimal reportedQty;

    @ApiModelProperty("入库数量")
    private BigDecimal inStockQty;

    @ApiModelProperty("原因")
    private String reason;

    @ApiModelProperty("是否完成报工")
    private Boolean finishReport;

    @ApiModelProperty("当前待上数工序（暂时无用）")
    private int routeNo;

    @ApiModelProperty("包装数量")
    private BigDecimal packQty;

    @ApiModelProperty("历史保存时的数量")
    private BigDecimal saveQty;

    @ApiModelProperty("裁床单工票信息明细汇报")
    @TableField(exist = false)
    private List<PrdCuttingTicketItemReport> cuttingTicketItemReports;

    @TableField(exist = false)
    @ApiModelProperty("颜色id")
    private String colorId;
    @TableField(exist = false)
    @ApiModelProperty("颜色")
    private String color;

    @TableField(exist = false)
    @ApiModelProperty("规格尺码id")
    private String specificationId;
    @TableField(exist = false)
    @ApiModelProperty("规格尺码")
    private String specification;

    @TableField(exist = false)
    @ApiModelProperty("工序总数")
    private int progressCount;
    @TableField(exist = false)
    @ApiModelProperty("已完成工序数")
    private int finishProgressCount;
    @TableField(exist = false)
    @ApiModelProperty("上数进度（已完成工序百分比）")
    private BigDecimal progress;
}
