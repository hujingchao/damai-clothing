package com.fenglei.model.oms.entity;

import java.util.List;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.UBillBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 销售出库单
 * </p>
 *
 * @author zjn
 * @since 2024-04-15
 */
@Getter
@Setter
@TableName("oms_sale_out_stock")
@ApiModel(value = "OmsSaleOutStock对象", description = "销售出库单")
public class OmsSaleOutStock extends UBillBaseEntity {

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

    @TableField(exist = false)
    private String stockerIds;


    @TableField(exist = false)
    private String inbillStatus;



    @ApiModelProperty("id集合，用于接收前台传参，如批量删除")
    @TableField(exist = false)
    private List<String> ids;

    @TableField(exist = false)
    private List<OmsSaleOutStockItem> saleOutStockItems;
}
