package com.fenglei.model.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 消息
 * </p>
 *
 * @author zhouyiqiu
 * @since 2022-04-13
 */
@Getter
@Setter
@TableName("sys_msg")
@ApiModel(value = "Msg对象", description = "消息")
public class SysMsg implements Serializable {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty("内码")
    @TableId(type = IdType.AUTO)
    private String id;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime = new Date();

    @ApiModelProperty("创建人")
    private String creator;

    @ApiModelProperty("创建人id")
    private String creatorId;

    @ApiModelProperty("消息类型：1、业务员 2、仓管员 3、客户联系人")
    private Integer type;

    @ApiModelProperty("消息标题")
    private String title;

    @ApiModelProperty("消息内容")
    private String content;

    @ApiModelProperty("具体业务单据单号")
    private String bizNo;

    @ApiModelProperty("具体业务单据id")
    private String bizId;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("是否已读")
    private Boolean isRead;

    @ApiModelProperty("单据类型 1、销售订单 2、装车单 3、出库单 4、退货单")
    private Integer billType;

}
