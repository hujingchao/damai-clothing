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
@ApiModel(value = "BdMaterialAttach-物料-附件", description = "物料-附件类")
@TableName(value = "bd_material_attach")
public class BdMaterialAttach extends BaseItemEntity {

    @ApiModelProperty("附件id")
    private String attachId;

    @TableField(exist = false)
    @ApiModelProperty("附件")
    private SysFiles sysFile;

    @ApiModelProperty("逻辑删除标识 0-未删除 1-已删除")
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;
}
