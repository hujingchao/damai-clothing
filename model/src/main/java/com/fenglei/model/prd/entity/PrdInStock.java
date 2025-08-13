package com.fenglei.model.prd.entity;

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
 * 入库单
 * </p>
 *
 * @author zgm
 * @since 2024-04-18
 */
@Getter
@Setter
@TableName("prd_in_stock")
@ApiModel(value = "PrdInStock对象", description = "入库单")
public class PrdInStock extends UBillBaseEntity {

    @ApiModelProperty("收货人id")
    private String receiveId;

    @ApiModelProperty("收货人")
    private String receiver;

    @ApiModelProperty("收货时间")
    private String receiveTime;

    @ApiModelProperty("备注信息")
    private String remark;

    @ApiModelProperty(value = "仓库id")
    private String repositoryId;
    @TableField(exist = false)
    @ApiModelProperty(value = "仓库名称")
    private String repositoryName;

    @ApiModelProperty(value = "货位id")
    private String positionId;
    @TableField(exist = false)
    @ApiModelProperty(value = "货位名称")
    private String positionName;

    @ApiModelProperty("入库单分录")
    @TableField(exist = false)
    private List<PrdInStockItem> itemList;


    @ApiModelProperty("id集合，用于接收前台传参，如批量删除")
    @TableField(exist = false)
    private List<String> ids;

    @TableField(exist = false)
    private String itemId;
}
