package com.fenglei.model.basedata;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@TableName("bd_customer_cate")
public class BdCustomerCate implements Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String name;

    @ApiModelProperty("逻辑删除标识 0-未删除 1-已删除")
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;
}
