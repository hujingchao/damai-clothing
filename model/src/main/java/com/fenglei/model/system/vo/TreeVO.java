package com.fenglei.model.system.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
public class TreeVO {

    private String id;

    private String label;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private List<TreeVO> children;

}
