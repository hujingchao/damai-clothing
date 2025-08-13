package com.fenglei.model.basedata;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.BaseItemEntity;
import com.fenglei.model.system.entity.SysFiles;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ljw
 */
@Data
@ApiModel(value = "BdTempMaterialAttach-物料-附件", description = "物料-附件类")
@TableName(value = "bd_temp_material_attach")
public class BdTempMaterialAttach extends BaseItemEntity {

    @ApiModelProperty("附件id")
    private String attachId;

    @TableField(exist = false)
    @ApiModelProperty("附件")
    private SysFiles sysFile;
}
