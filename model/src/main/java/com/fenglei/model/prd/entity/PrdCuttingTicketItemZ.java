package com.fenglei.model.prd.entity;

import java.util.List;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.UBillItemBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 裁床单票号种子表
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-12
 */
@Getter
@Setter
@TableName("prd_cutting_ticket_item_z")
@ApiModel(value = "PrdCuttingTicketItemZ对象", description = "裁床单票号种子表")
public class PrdCuttingTicketItemZ {

    @ApiModelProperty("流水号")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("任意文本")
    private String text;
}
