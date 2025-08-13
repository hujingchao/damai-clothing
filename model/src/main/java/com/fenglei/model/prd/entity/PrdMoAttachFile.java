package com.fenglei.model.prd.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.UBillItemBaseEntity;
import com.fenglei.model.system.entity.SysFiles;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ljw
 */
@Data
@ApiModel(value = "PrdMoAttachFile-生产订单 文件详情", description = "生产订单 文件详情类")
@TableName(value = "prd_mo_attach_file")
public class PrdMoAttachFile extends UBillItemBaseEntity {

    @ApiModelProperty("附件id")
    private String attachId;

    @TableField(exist = false)
    @ApiModelProperty("附件")
    private SysFiles sysFile;
}
