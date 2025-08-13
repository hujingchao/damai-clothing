package com.fenglei.model.inv.entity;

import java.util.List;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.UBillBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 入库后打包
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-23
 */
@Getter
@Setter
@TableName("inv_package")
@ApiModel(value = "InvPackage对象", description = "入库后打包")
public class InvPackage extends UBillBaseEntity {

    @ApiModelProperty("包装人id")
    private String packerId;

    @ApiModelProperty("包装人")
    private String packer;

    @ApiModelProperty("包装时间")
    private String packTime;

    @ApiModelProperty("备注信息")
    private String remark;



    @ApiModelProperty("id集合，用于接收前台传参，如批量删除")
    @TableField(exist = false)
    private List<String> ids;

    @ApiModelProperty("包装单分录")
    @TableField(exist = false)
    private List<InvPackageItem> packageItems;

    @ApiModelProperty("包号信息")
    @TableField(exist = false)
    private List<InvPackageNo> packageNos;
}
