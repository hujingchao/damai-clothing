package com.fenglei.model.system.dto;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PictureDTO {

    private String id;

    /**
     * 文件名
     */
    private String fileName;
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
}
