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
 * 裁床单辅料出库记录
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-17
 */
@Getter
@Setter
@TableName("prd_cutting_aux_io")
@ApiModel(value = "PrdCuttingAuxIo对象", description = "裁床单辅料出库记录")
public class PrdCuttingAuxIo extends UBillItemBaseEntity {

    @ApiModelProperty(value = "物料详情id")
    private String materialDetailId;

    @ApiModelProperty("实际用量（KG）")
    private BigDecimal qty;

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
