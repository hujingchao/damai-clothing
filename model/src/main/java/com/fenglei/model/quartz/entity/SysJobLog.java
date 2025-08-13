package com.fenglei.model.quartz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fenglei.model.base.pojo.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * 定时任务调度日志表 sys_job_log
 *
 * @author ruoyi
 */
@Data
@TableName("sys_job_log")
@ApiModel(value = "SysJobLog对象", description = "")
public class SysJobLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 任务名称 */
//    @Excel(name = "任务名称")
    @NotBlank(message = "任务名称不能为空")
    @ApiModelProperty(value = "任务名称")
    private String name;

    /** 任务组名 */
//    @Excel(name = "任务组名")
    @ApiModelProperty(value = "任务组名")
    private String jobGroup;

    /** 调用目标字符串 */
//    @Excel(name = "调用目标字符串")
    @ApiModelProperty(value = "调用目标字符串")
    private String invokeTarget;

    /** 日志信息 */
//    @Excel(name = "日志信息")
    @ApiModelProperty(value = "日志信息")
    private String jobMessage;

    /** 执行状态（0正常 1失败） */
//    @Excel(name = "执行状态", readConverterExp = "0=正常,1=失败")
    @ApiModelProperty(value = "执行状态 0=正常,1=失败")
    private String status;

    /** 异常信息 */
//    @Excel(name = "异常信息")
    @ApiModelProperty(value = "异常信息")
    private String exceptionInfo;

    /** 开始时间 */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "开始时间")
    @TableField(exist = false)
    private Date startTime;

    /** 停止时间 */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "停止时间")
    @TableField(exist = false)
    private Date stopTime;


    /** 查询开始时间 */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "查询开始时间")
    @TableField(exist = false)
    private Date beginTime;

    /** 查询结束时间 */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "查询结束时间")
    @TableField(exist = false)
    private Date endTime;



    @ApiModelProperty("id集合，用于接收前台传参，如批量删除")
    @TableField(exist = false)
    private List<String> ids;
}
