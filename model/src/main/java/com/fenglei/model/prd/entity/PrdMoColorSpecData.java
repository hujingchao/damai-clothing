package com.fenglei.model.prd.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.UBillItemBaseEntity;
import com.fenglei.model.basedata.BdMaterialAttach;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ljw
 */
@Data
@ApiModel(value = "PrdMoColor-生产订单 颜色规格详情", description = "生产订单 颜色规格详情类")
@TableName(value = "prd_mo_color_spec_data")
public class PrdMoColorSpecData extends UBillItemBaseEntity {

    @TableField(exist = false)
    @ApiModelProperty("主图")
    private String mainPicUrl;

    @TableField(exist = false)
    @ApiModelProperty(value = "图片信息")
    List<BdMaterialAttach> attaches;
    @TableField(exist = false)
    List<String> attachUrls;

    @ApiModelProperty("产品id，成品的deltailId，即skuId")
    private String productId;
    @TableField(exist = false)
    @ApiModelProperty("大货款号，编码")
    private String productNum;
    @TableField(exist = false)
    @ApiModelProperty("大货款号，名称")
    private String productName;
    @TableField(exist = false)
    @ApiModelProperty("产品ids")
    private List<String> productIds;

    @TableField(exist = false)
    @ApiModelProperty("单位id")
    private String unitId;
    @TableField(exist = false)
    @ApiModelProperty(value = "单位")
    private String unitName;

    @ApiModelProperty("单价")
    private BigDecimal price;

    @ApiModelProperty(value = "颜色/色号")
    private String color;

    @ApiModelProperty("规格")
    private String specification;

    @ApiModelProperty("数量")
    private BigDecimal qty;

    @ApiModelProperty("裁床单数量")
    private BigDecimal cuttingQty;

    @ApiModelProperty("入库数量")
    private BigDecimal instockQty;

    @TableField(exist = false)
    private List<PrdCuttingTicket> ticketList;
}
