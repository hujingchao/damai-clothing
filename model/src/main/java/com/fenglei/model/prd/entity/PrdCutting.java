package com.fenglei.model.prd.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.UBillBaseEntity;
import com.fenglei.model.workFlow.entity.ChildNode;
import com.fenglei.model.workFlow.entity.ChildNodeApprovalResult;
import com.fenglei.model.workFlow.entity.WorkFlow;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 裁床单 - 主体
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-02
 */
@Getter
@Setter
@TableName("prd_cutting")
@ApiModel(value = "PrdCutting对象", description = "裁床单 - 主体")
public class PrdCutting extends UBillBaseEntity {

    @ApiModelProperty("大货款号（成品id）")
    private String productId;

    @ApiModelProperty("机床号（设备id）")
    private String equipmentId;

    @ApiModelProperty("交货日期")
    private String deliveryDate;

    @ApiModelProperty("裁床日期")
    private String cuttingDate;

    @ApiModelProperty("统计编菲总数")
    private BigDecimal sumWeavingQty;

    @ApiModelProperty("生产数量")
    private BigDecimal productQty;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("单位id")
    private String unitId;

    @ApiModelProperty("完成数量（入库数量）")
    private BigDecimal finishQty;

    @ApiModelProperty("已汇报数量")
    private BigDecimal reportedQty;

    @ApiModelProperty("是否已完工（所有末道工序都已汇报）")
    private boolean reportedStatus;

    @ApiModelProperty("源单id")
    private String srcId;
    @ApiModelProperty("源单单号")
    private String srcNo;
    @ApiModelProperty("源单类型")
    private String srcType;


    @TableField(exist = false)
    @ApiModelProperty("大货款号，编码")
    private String productNum;
    @TableField(exist = false)
    @ApiModelProperty("大货款号，名称")
    private String productName;
    @TableField(exist = false)
    @ApiModelProperty("主图")
    private List<String> mainPics;
    @TableField(exist = false)
    @ApiModelProperty("主图")
    private String mainPic;
    @TableField(exist = false)
    @ApiModelProperty("主图id")
    private String mainPicId;

    @TableField(exist = false)
    @ApiModelProperty("工序总数")
    private int progressCount;
    @TableField(exist = false)
    @ApiModelProperty("已完成工序数")
    private int finishProgressCount;
    @TableField(exist = false)
    @ApiModelProperty("上数进度（已完成工序百分比）")
    private BigDecimal progress;

    @TableField(exist = false)
    @ApiModelProperty("机床号，编号")
    private String equipmentNum;

    @ApiModelProperty("id集合，用于接收前台传参，如批量删除")
    @TableField(exist = false)
    private List<String> ids;

    @ApiModelProperty("裁床单 - 工票信息")
    @TableField(exist = false)
    private List<PrdCuttingTicket> cuttingTickets;


    @ApiModelProperty("裁床单 - 工序信息")
    @TableField(exist = false)
    private List<PrdCuttingRoute> cuttingRoutes;


    @ApiModelProperty("裁床单 - 原材料信息")
    @TableField(exist = false)
    private List<PrdCuttingRaw> cuttingRaws;
}
