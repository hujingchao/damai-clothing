package com.fenglei.model.base.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BaseItemEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 内码
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String pid;

    @TableField(exist = false)
    private List<String> pids;

    @TableField(exist = false)
    private List<String> ids;
}
