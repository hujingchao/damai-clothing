package com.fenglei.model.inv.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.UBillBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
/**
 * <p>
 * 其他入库单
 * </p>
 *
 * @author zgm
 * @since 2024-04-28
 */
@Getter
@Setter
@TableName("inv_other_in_stock")
@ApiModel(value = "InvOtherInStock对象", description = "其他入库单")
public class InvOtherInStock  extends UBillBaseEntity {

    @ApiModelProperty("出库日期")
    private String inStockDate;

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
    private List<InvOtherInStockItem> itemList;

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
