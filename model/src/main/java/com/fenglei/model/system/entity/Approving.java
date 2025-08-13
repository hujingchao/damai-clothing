package com.fenglei.model.system.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author ljw
 */
@Data
public class Approving {

    @ApiModelProperty(value = "流程名称")
    private String name;
    @ApiModelProperty(value = "表单名称")
    private String formName;
    @ApiModelProperty(value = "表单id")
    private String tableInfoId;

    @ApiModelProperty(value = "编码/单据编号")
    private String number;
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "申请时间")
    @TableField(fill = FieldFill.UPDATE)
    private String applicationTime;

    @ApiModelProperty(value = "申请人id")
    private String applicantBy;

    @ApiModelProperty(value = "申请人")
    private String applicant;

    @ApiModelProperty(value = "状态：0-未提交；1-流转中；2-已完成；3-重新提交")
    private Integer status;

    @ApiModelProperty(value = "当前节点id")
    private String nodeUserId;

    @ApiModelProperty(value = "当前节点")
    private String nodeUser;
}
