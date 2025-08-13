package com.fenglei.model.prd.entity;

import java.util.List;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.UBillItemBaseEntity;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 裁床单 - 工票信息明细汇报汇报
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-02
 */
@Getter
@Setter
@TableName("prd_cutting_ticket_item_report")
@ApiModel(value = "PrdCuttingTicketItemReport对象", description = "裁床单 - 工票信息明细汇报汇报")
public class PrdCuttingTicketItemReport extends UBillItemBaseEntity {

    @ApiModelProperty("裁床单工序id")
    private String cuttingRouteId;

    @ApiModelProperty("工序号")
    private int procedureNo;

    @ApiModelProperty("工序id")
    private String procedureId;

    @ApiModelProperty("工价(元)")
    private BigDecimal price;

    //入库后会修改 用于计算工资的数量
    @ApiModelProperty("已上数数量")
    private BigDecimal reportedQty;

    //实际汇报时的数量
    @ApiModelProperty("工序汇报时的数量")
    private BigDecimal realReportedQty;

    @ApiModelProperty("是否收货入库")
    private Boolean isInStock;

    @ApiModelProperty("上数人id")
    private String reporterId;

    @ApiModelProperty("上数时间")
    private String reportTime;

    @ApiModelProperty("备注")
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建人id")
    private String creatorId;



    @ApiModelProperty("id集合，用于接收前台传参，如批量删除")
    @TableField(exist = false)
    private List<String> ids;


    @TableField(exist = false)
    @ApiModelProperty("工序名称")
    private String procedureName;
    @TableField(exist = false)
    @ApiModelProperty("工序编码")
    private String procedureNum;

    @TableField(exist = false)
    @ApiModelProperty("上数人名称")
    private String reporterName;

    @TableField(exist = false)
    @ApiModelProperty("本次上数数量（汇报数量）")
    private BigDecimal reportingQty;

    @TableField(exist = false)
    @ApiModelProperty("裁床单id")
    private String cuttingId;
}
