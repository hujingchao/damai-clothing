package com.fenglei.model.fin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 年计件工资Dto
 */
@Data
public class YearColumnsDto {

    @ApiModelProperty("年")
    private int year;

    private List<Integer> month;
}
