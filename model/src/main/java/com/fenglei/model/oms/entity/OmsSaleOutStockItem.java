package com.fenglei.model.oms.entity;

import java.math.BigDecimal;
import java.util.List;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.UBillItemBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 销售出库单分录
 * </p>
 *
 * @author zjn
 * @since 2024-04-15
 */
@Getter
@Setter
@TableName("oms_sale_out_stock_item")
@ApiModel(value = "OmsSaleOutStockItem对象", description = "销售出库单分录")
public class OmsSaleOutStockItem extends UBillItemBaseEntity {

    @ApiModelProperty("包号（批号）")
    private String lot;

    @ApiModelProperty("仓库")
    private String repositoryId;

    @ApiModelProperty("仓库名称")
    @TableField(exist = false)
    private String repositoryName;

    @ApiModelProperty("仓位")
    private String positionId;

    @ApiModelProperty("仓位")
    @TableField(exist = false)
    private String positionName;

    @ApiModelProperty("数量")
    private BigDecimal qty;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("金额")
    private String amount;


    @ApiModelProperty("id集合，用于接收前台传参，如批量删除")
    @TableField(exist = false)
    private List<String> ids;

    @TableField(exist = false)
    private List<OmsSaleOutStockItemDetail> saleOutStockItemDetails;
}
