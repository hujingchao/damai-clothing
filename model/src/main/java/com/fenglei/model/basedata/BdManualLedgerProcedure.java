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
@ApiModel(value = "BdManualLedgerProcedure-手工帐(工序信息)", description = "手工帐(工序信息)类")
@TableName(value = "bd_manual_ledger_procedure")
public class BdManualLedgerProcedure extends BaseItemEntity {

    @ApiModelProperty(value = "工序id")
    private String procedureId;
    @TableField(exist = false)
    @ApiModelProperty(value = "工序编码")
    private String procedureNumber;
    @TableField(exist = false)
    @ApiModelProperty(value = "工序名称")
    private String procedureName;
    @TableField(exist = false)
    @ApiModelProperty(value = "工序说明")
    private String procedureRemark;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private String createTime;
    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String creator;
    /**
     * 创建人id
     */
    @TableField(fill = FieldFill.INSERT)
    private String creatorId;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private String updateTime;
    /**
     * 更新人
     */
    @TableField(fill = FieldFill.UPDATE)
    private String updater;
    /**
     * 修改人id
     */
    @TableField(fill = FieldFill.UPDATE)
    private String updaterId;

    @ApiModelProperty("逻辑删除标识 0-未删除 1-已删除")
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;
}
