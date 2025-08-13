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
 * 裁床单 - 工序
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-03
 */
@Getter
@Setter
@TableName("prd_cutting_route")
@ApiModel(value = "PrdCuttingRoute对象", description = "裁床单 - 工序")
public class PrdCuttingRoute extends UBillItemBaseEntity {

    @ApiModelProperty("工序号")
    private Integer procedureNo;

    @ApiModelProperty("工序id")
    private String procedureId;

    @ApiModelProperty("工价(元)")
    private BigDecimal price;

    @ApiModelProperty("工序数量")
    private BigDecimal qty;

    @ApiModelProperty("已汇报数量(所有票号)")
    private BigDecimal reportedQty;



    @ApiModelProperty("id集合，用于接收前台传参，如批量删除")
    @TableField(exist = false)
    private List<String> ids;


    @ApiModelProperty("生产订单工序数量")
    @TableField(exist = false)
    private BigDecimal moProcessQty;

    @TableField(exist = false)
    @ApiModelProperty("工序名称")
    private String procedureName;
    @TableField(exist = false)
    @ApiModelProperty("工序编码")
    private String procedureNum;
    @TableField(exist = false)
    @ApiModelProperty("生产工序id")
    private String uniqueKey;


    @TableField(exist = false)
    @ApiModelProperty("汇报详情")
    private List<PrdCuttingTicketItemReport> cuttingTicketItemReports;
}
