package com.fenglei.model.basedata;

import com.baomidou.mybatisplus.annotation.FieldFill;
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
@ApiModel(value = "BdMaterialProcess-物料-工艺要求", description = "物料-工艺要求类")
@TableName(value = "bd_material_process")
public class BdMaterialProcess extends BaseItemEntity {

    @ApiModelProperty(value = "附件id")
    @TableField(fill = FieldFill.UPDATE)
    private String attachId;

    @TableField(exist = false)
    @ApiModelProperty(value = "附件")
    private SysFiles sysFile;

    @ApiModelProperty(value = "工艺名称")
    private String process;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty("逻辑删除标识 0-未删除 1-已删除")
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;
}
