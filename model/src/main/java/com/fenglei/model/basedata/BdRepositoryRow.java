package com.fenglei.model.basedata;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.BaseItemEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ljw
 */
@Data
@ApiModel(value = "BdRepositoryRow-仓库-行", description = "仓库-行类")
@TableName(value = "bd_repository_row")
public class BdRepositoryRow extends BaseItemEntity {

    @ApiModelProperty(value = "行")
    private String name;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private String createTime;
    /**
     * 创建人
     */
    @TableField(exist = false)
    private String creator;
    /**
     * 创建人id
     */
    @TableField(fill = FieldFill.INSERT)
    private String creatorId;

    @ApiModelProperty("逻辑删除标识 0-未删除 1-已删除")
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;
}
