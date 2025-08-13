package com.fenglei.model.prd.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.UBillItemBaseEntity;
import com.fenglei.model.system.entity.SysFiles;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ljw
 */
@Data
@ApiModel(value = "PrdMoAttachImg-生产订单 图片详情", description = "生产订单 图片详情类")
@TableName(value = "prd_mo_attach_img")
public class PrdMoAttachImg extends UBillItemBaseEntity {

    @ApiModelProperty("附件id")
    private String attachId;

    @TableField(exist = false)
    @ApiModelProperty("附件")
    private SysFiles sysFile;
}
