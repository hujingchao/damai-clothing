package com.fenglei.model.basedata;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.BaseItemEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ljw
 */
@Data
@ApiModel(value = "BdTempWagesItem-工价明细临时", description = "工价明细临时类")
@TableName(value = "bd_temp_wages_item")
public class BdTempWagesItem extends BaseItemEntity {

    @ApiModelProperty("工序号")
    private Integer no;

    @ApiModelProperty(value = "工序id")
    private String procedureId;

    @TableField(exist = false)
    @ApiModelProperty(value = "工序编码")
    private String procedureNumber;

    @TableField(exist = false)
    @ApiModelProperty(value = "工序名称")
    private String procedureName;

    @TableField(exist = false)
    @ApiModelProperty(value = "工序描述")
    private String procedureRemark;

    @ApiModelProperty(value = "工价")
    private BigDecimal price;

    @ApiModelProperty(value = "备注")
    @TableField(fill = FieldFill.UPDATE)
    private String remark;

    @TableField(exist = false)
    private List<String> pids;

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
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private String updateTime;
    /**
     * 更新人
     */
    @TableField(exist = false)
    private String updater;
    /**
     * 修改人id
     */
    @TableField(fill = FieldFill.UPDATE)
    private String updaterId;
}
