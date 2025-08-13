package com.fenglei.model.basedata;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fenglei.model.base.pojo.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author ljw
 */
@Data
@ApiModel(value = "BdCustomer-客户", description = "客户类")
public class BdCustomer extends BaseEntity {

    @ApiModelProperty(value = "编码")
    private String number;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "简称")
    private String simpleName;

    @ApiModelProperty("分类id")
    private String customerCateId;
    @TableField(exist = false)
    @ApiModelProperty("分类名称")
    private String customerCateName;
    @TableField(exist = false)
    @ApiModelProperty("分类id")
    private List<String> customerCateIds;

    @ApiModelProperty(value = "联系电话")
    @TableField(fill = FieldFill.UPDATE)
    private String contactNumber;
    @ApiModelProperty("传真")
    @TableField(fill = FieldFill.UPDATE)
    private String fax;

    @ApiModelProperty(value = "联系地址")
    @TableField(fill = FieldFill.UPDATE)
    private String address;

    @ApiModelProperty("逻辑删除标识 0-未删除 1-已删除")
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;
}
