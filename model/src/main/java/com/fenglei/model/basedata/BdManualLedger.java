package com.fenglei.model.basedata;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.BaseEntity;
import com.fenglei.model.system.entity.SysUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author ljw
 */
@Data
@ApiModel(value = "BdManualLedger-手工帐", description = "手工帐类")
@TableName(value = "bd_manual_ledger")
public class BdManualLedger extends BaseEntity {

    @ApiModelProperty(value = "编码")
    private String number;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "备注")
    @TableField(fill = FieldFill.UPDATE)
    private String remark;

    @ApiModelProperty(value = "负责人id")
    private String leaderId;
    @TableField(exist = false)
    @ApiModelProperty(value = "负责人")
    private SysUser leader;

    @TableField(exist = false)
    @ApiModelProperty(value = "工序信息")
    private List<BdManualLedgerProcedure> procedures;

    @TableField(exist = false)
    @ApiModelProperty(value = "员工信息")
    private List<BdManualLedgerStaff> staffs;

    @TableField(exist = false)
    @ApiModelProperty("工序id")
    private String procedureId;

    @TableField(exist = false)
    @ApiModelProperty("员工id")
    private String staffId;

    @ApiModelProperty("逻辑删除标识 0-未删除 1-已删除")
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;
}
