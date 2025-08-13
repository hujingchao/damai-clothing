package com.fenglei.model.basedata;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author ljw
 */
@Data
@ApiModel(value = "BdSupplier-供应商", description = "供应商类")
@TableName(value = "bd_supplier")
public class BdSupplier extends BaseEntity {

    @ApiModelProperty(value = "编码")
    private String number;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "联系人")
    private String contact;
    @ApiModelProperty(value = "联系电话")
    private String contactNumber;
    @ApiModelProperty(value = "联系地址")
    @TableField(fill = FieldFill.UPDATE)
    private String address;
    @ApiModelProperty(value = "备注")
    @TableField(fill = FieldFill.UPDATE)
    private String remark;

    @ApiModelProperty("逻辑删除标识 0-未删除 1-已删除")
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;
}
