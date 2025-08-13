package com.fenglei.model.system.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class TypeTreeVo {

    private String id;

    private String name;

    @ApiModelProperty("类别：all-全部；prov-省份；city-城市；region-区域；rdtl-小区；build-楼幢；")
    private String type;

    private String parentId;

    private String parentName;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private List<TypeTreeVo> children;
}
