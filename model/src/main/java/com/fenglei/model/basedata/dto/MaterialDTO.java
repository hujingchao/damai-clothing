package com.fenglei.model.basedata.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MaterialDTO {
    private String materialDetailId;
    private String materialId;
    private String materialNumber;
    private String materialName;
    private String materialGroup;
    private String unitId;
    private String unitName;
    private String mainPicId;
    private String mainPicUrl;
    private String colorId;
    private String color;
    private String specificationId;
    private String specification;
    private String repositoryName;
    private String positionName;
    private String creator;
    private String updater;
    private String lot;
    private BigDecimal qty;
    private BigDecimal piQty;

    @ApiModelProperty("库存id")
    private String invId;

    @ApiModelProperty("仓库")
    private String repositoryId;

    @ApiModelProperty("仓位")
    private String positionId;

    private BigDecimal price;

    private String materialDetailNumber;


}
