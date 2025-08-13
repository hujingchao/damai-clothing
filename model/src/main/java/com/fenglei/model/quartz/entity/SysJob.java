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
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * 定时任务调度表 sys_job
 *
 * @author ruoyi
 */
@Data
@Accessors(chain = true)
@TableName("sys_job")
@ApiModel(value = "SysJob对象", description = "")
public class SysJob extends BaseEntity {

    /**
     * 任务名称
     */
//    @Excel(name = "任务名称")
    @NotBlank(message = "任务名称不能为空")
    @ApiModelProperty(value = "任务名称")
    private String name;

    /**
     * 任务组名
     */
//    @Excel(name = "任务组名")
    @ApiModelProperty(value = "任务组名")
    private String jobGroup;

    /**
     * 调用目标字符串
     */
//    @Excel(name = "调用目标字符串")
    @NotBlank(message = "调用目标字符串不能为空")
    @Size(min = 0, max = 500, message = "调用目标字符串长度不能超过500个字符")
    @ApiModelProperty(value = "调用目标字符串")
    private String invokeTarget;

    /**
     * cron执行表达式
     */
//    @Excel(name = "执行表达式 ")
    @NotBlank(message = "Cron执行表达式不能为空")
    @Size(min = 0, max = 255, message = "Cron执行表达式不能超过255个字符")
    @ApiModelProperty(value = "Cron执行表达式不能为空")
    private String cronExpression;

    /**
     * cron计划策略
     */
//    @Excel(name = "计划策略 ", readConverterExp = "0=默认,1=立即触发执行,2=触发一次执行,3=不触发立即执行")
    @ApiModelProperty(value = "计划策略 0=默认,1=立即触发执行,2=触发一次执行,3=不触发立即执行")
    private String misfirePolicy;

    /**
     * 是否并发执行（0允许 1禁止）
     */
//    @Excel(name = "并发执行", readConverterExp = "0=允许,1=禁止")
    @ApiModelProperty(value = "并发执行 0=允许,1=禁止")
    private String concurrent;

    /**
     * 任务状态（0正常 1暂停）
     */
//    @Excel(name = "任务状态", readConverterExp = "0=正常,1=暂停")
    @ApiModelProperty(value = "任务状态 0=正常,1=暂停")
    private String status;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "下次执行时间")
    @TableField(exist = false)
    private Date nextValidTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "单据类型")
    private String billType;

    @ApiModelProperty(value = "单据id")
    private String billId;
}
