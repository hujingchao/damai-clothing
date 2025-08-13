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
 * 裁床单原材料出库记录
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-15
 */
@Getter
@Setter
@TableName("prd_cutting_raw_io")
@ApiModel(value = "PrdCuttingRawIo对象", description = "裁床单原材料出库记录")
public class PrdCuttingRawIo extends UBillItemBaseEntity {

    @ApiModelProperty("原材料id")
    private String materialDetailId;

    @ApiModelProperty("裁床单id")
    private String gpId;

    @ApiModelProperty("实际用量（KG）")
    private BigDecimal qty;

    @ApiModelProperty("实际用量（匹数）")
    private BigDecimal piQty;

    @ApiModelProperty("仓库")
    private String repositoryId;

    @ApiModelProperty("仓位")
    private String positionId;

    @ApiModelProperty("出库价")
    private BigDecimal price;

    @ApiModelProperty("批号")
    private String lot;



    @ApiModelProperty("id集合，用于接收前台传参，如批量删除")
    @TableField(exist = false)
    private List<String> ids;
}
