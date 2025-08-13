package com.fenglei.model.prd.entity;

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
 * 包装单
 * </p>
 *
 * @author zgm
 * @since 2024-04-11
 */
@Getter
@Setter
@TableName("prd_packing")
@ApiModel(value = "PrdPacking对象", description = "包装单")
public class PrdPacking extends UBillBaseEntity {

    @ApiModelProperty("包装人Id")
    private String packerId;

    @ApiModelProperty("包装人")
    private String packer;

    @ApiModelProperty("包装时间")
    private String packTime;

    @ApiModelProperty("备注信息")
    private String remark;


    @ApiModelProperty("包装单分录")
    @TableField(exist = false)
    private List<PrdPackingItem> itemList;


    @ApiModelProperty("id集合，用于接收前台传参，如批量删除")
    @TableField(exist = false)
    private List<String> ids;

    @TableField(exist = false)
    private String itemId;
}
