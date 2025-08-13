package com.fenglei.model.inv.entity;

import java.util.List;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.UBillBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 其他出库单
 * </p>
 *
 * @author zgm
 * @since 2024-04-28
 */
@Getter
@Setter
@TableName("inv_other_out_stock")
@ApiModel(value = "InvOtherOutStock对象", description = "其他出库单")
public class InvOtherOutStock extends UBillBaseEntity {

    @ApiModelProperty("出库日期")
    private String outStockDate;

    @ApiModelProperty("仓管员")
    private String stockerId;

    @ApiModelProperty("仓管员")
    @TableField(exist = false)
    private String stockerName;

    @TableField(exist = false)
    @ApiModelProperty("出库日期(开始)，用于过滤")
    private String bizDateBegin;
    @TableField(exist = false)
    @ApiModelProperty("出库日期(结束)，用于过滤")
    private String bizDateEnd;

    private String remark;

    @ApiModelProperty("id集合，用于接收前台传参，如批量删除")
    @TableField(exist = false)
    private List<InvOtherOutStockItem> itemList;

    @TableField(exist = false)
    private String stockerIds;
    @TableField(exist = false)
    private List<String> stockerIdList;

    @TableField(exist = false)
    private String inbillStatus;
    @TableField(exist = false)
    private List<String> inbillStatusList;



    @ApiModelProperty("id集合，用于接收前台传参，如批量删除")
    @TableField(exist = false)
    private List<String> ids;
}
