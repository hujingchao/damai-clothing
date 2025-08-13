package com.fenglei.model.pur.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.UBillBaseEntity;
import com.fenglei.model.system.entity.SysUser;
import com.fenglei.model.workFlow.entity.ChildNode;
import com.fenglei.model.workFlow.entity.ChildNodeApprovalResult;
import com.fenglei.model.workFlow.entity.WorkFlow;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@TableName("pur_purchase_instock")
@ApiModel(value = "PurPurchaseInstock对象", description = "采购入库单 - 主体")
public class PurPurchaseInstock extends UBillBaseEntity {

    @ApiModelProperty("入库日期")
    private String bizDate;
    @TableField(exist = false)
    @ApiModelProperty("入库日期(开始)，用于过滤")
    private String bizDateBegin;
    @TableField(exist = false)
    @ApiModelProperty("入库日期(结束)，用于过滤")
    private String bizDateEnd;

    @ApiModelProperty("仓管员id")
    private String stockerId;
    @TableField(exist = false)
    @ApiModelProperty(value = "仓管员")
    private String stocker;
    @TableField(exist = false)
    @ApiModelProperty("仓管员id")
    private List<String> stockerIds;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("源单id")
    private String srcId;
    @ApiModelProperty("源单编码")
    private String srcNo;
    @ApiModelProperty("源单类型")
    private String srcType;
    @TableField(exist = false)
    @ApiModelProperty("源单类型，用于过滤")
    private List<String> srcTypes;


    @ApiModelProperty("id集合，用于接收前台传参，如批量删除")
    @TableField(exist = false)
    private List<String> ids;

    @ApiModelProperty("明细信息")
    @TableField(exist = false)
    private List<PurPurchaseInstockItem> purchaseInstockItems;


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
    private String itemProductId;
    @TableField(exist = false)
    private List<String> itemProductIds;
    @TableField(exist = false)
    private String itemSupplierId;
    @TableField(exist = false)
    private List<String> itemSupplierIds;
    @TableField(exist = false)
    private String itemRepositoryId;
    @TableField(exist = false)
    private List<String> itemRepositoryIds;
}
