package com.fenglei.model.basedata;

import com.baomidou.mybatisplus.annotation.*;
import com.fenglei.model.base.pojo.BaseItemEntity;
import com.fenglei.model.system.entity.SysFiles;
import com.fenglei.model.workFlow.entity.ChildNode;
import com.fenglei.model.workFlow.entity.ChildNodeApprovalResult;
import com.fenglei.model.workFlow.entity.WorkFlow;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ljw
 */
@Data
@ApiModel(value = "BdMaterialDetail-物料详细", description = "物料详细类")
@TableName(value = "bd_material_detail")
public class BdMaterialDetail extends BaseItemEntity {

    @ApiModelProperty(value = "商家编码")
    private String number;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "颜色/色号id")
    private String colorId;

    @TableField(exist = false)
    @ApiModelProperty(value = "颜色/色号")
    private String color;

    @ApiModelProperty(value = "规格id")
    private String specificationId;

    @TableField(exist = false)
    @ApiModelProperty(value = "规格")
    private String specification;

    @ApiModelProperty("单价(元)")
    private BigDecimal price;

    @ApiModelProperty("逻辑删除标识 0-未删除 1-已删除")
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;

    @TableField(exist = false)
    @ApiModelProperty("原始id")
    private String originalId;

    @ApiModelProperty(value = "状态：0-未提交；1-流转中；2-已完成；3-重新提交")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private String createTime;
    /**
     * 创建人
     */
    @TableField(exist = false)
    private String creator;
    /**
     * 创建人id
     */
    @TableField(fill = FieldFill.INSERT)
    private String creatorId;

    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty("更新时间")
    private String updateTime;

    @TableField(exist = false)
    @ApiModelProperty("更新人")
    private String updater;

    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty("修改人id")
    private String updaterId;

    @ApiModelProperty(value = "最终审核时间")
    @TableField(fill = FieldFill.UPDATE)
    private String auditTime;

    @ApiModelProperty(value = "最终审核人姓名")
    @TableField(exist = false)
    private String auditor;

    @ApiModelProperty(value = "最终审核人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String auditorId;

    @ApiModelProperty(value = "主图id")
    @TableField(fill = FieldFill.UPDATE)
    private String mainPicId;
    @TableField(exist = false)
    @ApiModelProperty("主图")
    private SysFiles mainPic;
    @TableField(exist = false)
    @ApiModelProperty("主图Url")
    private String mainPicUrl;

    @ApiModelProperty("节点审批条件")
    @TableField(exist = false)
    private ChildNodeApprovalResult childNodeApprovalResult;
    @ApiModelProperty("流程ID")
    @TableField(exist = false)
    private String workFlowId;
    @ApiModelProperty("流程配置类")
    @TableField(exist = false)
    private WorkFlow workFlow;
    @ApiModelProperty("当前节点")
    @TableField(exist = false)
    private String currentNodeId;
    @ApiModelProperty("抄送节点")
    @TableField(exist = false)
    private List<ChildNode> childNodes;
    @ApiModelProperty("流程实例状态")
    @TableField(exist = false)
    private Integer workFlowInstantiateStatus;
    @ApiModelProperty("发起人id")
    @TableField(exist = false)
    private String userId;
    @TableField(exist = false)
    private Integer nodeStatus;

    @TableField(exist = false)
    private BigDecimal qty;
}
