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
@ApiModel(value = "BdRepository-仓库", description = "仓库类")
@TableName(value = "bd_repository")
public class BdRepository extends BaseEntity {
    @ApiModelProperty(value = "编码")
    private String no;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "地址")
    private String address;
    @ApiModelProperty(value = "备注")
    @TableField(fill = FieldFill.UPDATE)
    private String remark;

    @ApiModelProperty(value = "是否启用货位")
    private Boolean usePosition;

    @ApiModelProperty("逻辑删除标识 0-未删除 1-已删除")
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;

    @TableField(exist = false)
    @ApiModelProperty(value = "仓位数")
    private Integer positionCount;

    @TableField(exist = false)
    private String keyword;

    @TableField(exist = false)
    List<BdRepositoryShelf> shelfList;

    @TableField(exist = false)
    List<BdRepositoryRow> rowList;

    @TableField(exist = false)
    List<BdRepositoryColumn> columnList;

    @TableField(exist = false)
    private List<BdPosition> positions;
}
