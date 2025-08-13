package com.fenglei.model.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description: 文件
 */
@Data
@TableName(value = "sys_files")
public class SysFiles extends BaseEntity {

    private static final long serialVersionUID = -8052607158756576093L;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件类型
     */
    private String fileType;
    /**
     * url
     */
    private String url;

    /**
     * 大小
     */
    private String size;
    /**
     * 高度
     */
    private String height;
    /**
     * 宽度
     */
    private String width;

    private String batchId;

    private int sort;

    private String filePath;

    @ApiModelProperty("禁止下载：0-否；1-是；")
    private Boolean disableDownload;

    @ApiModelProperty("别名")
    private String alias;
    @ApiModelProperty("说明")
    private String remark;
}
