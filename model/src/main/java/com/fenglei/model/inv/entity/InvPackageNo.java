package com.fenglei.model.inv.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.UBillItemBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 打包单包号明细
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-23
 */
@Getter
@Setter
@TableName("inv_package_no")
@ApiModel(value = "InvPackageNo对象", description = "打包单包号明细")
public class InvPackageNo extends UBillItemBaseEntity {

    @ApiModelProperty("包号")
    private String packageNo;
}
