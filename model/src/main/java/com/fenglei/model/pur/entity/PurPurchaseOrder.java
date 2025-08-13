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
@TableName("pur_purchase_order")
@ApiModel(value = "PurPurchaseOrder对象", description = "采购订单 - 主体")
public class PurPurchaseOrder extends UBillBaseEntity {

    @ApiModelProperty("单据类型")
    private String billType;
    @TableField(exist = false)
    @ApiModelProperty("单据类型，用于过滤")
    private List<String> billTypes;

    @ApiModelProperty("采购日期")
    private String bizDate;
    @TableField(exist = false)
    @ApiModelProperty("采购日期(开始)，用于过滤")
    private String bizDateBegin;
    @TableField(exist = false)
    @ApiModelProperty("采购日期(结束)，用于过滤")
    private String bizDateEnd;

    @ApiModelProperty("采购员id")
    private String purchaserId;
    @TableField(exist = false)
    @ApiModelProperty(value = "采购员")
    private String purchaser;
    @TableField(exist = false)
    @ApiModelProperty("跟单员id")
    private List<String> purchaserIds;

    @ApiModelProperty("跟单员id")
    private String followerId;
    @TableField(exist = false)
    @ApiModelProperty(value = "跟单员")
    private String follower;
    @TableField(exist = false)
    @ApiModelProperty("跟单员id")
    private List<String> followerIds;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("id集合，用于接收前台传参，如批量删除")
    @TableField(exist = false)
    private List<String> ids;

    @ApiModelProperty("明细信息")
    @TableField(exist = false)
    private List<PurPurchaseOrderItem> purchaseOrderItems;


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
    private Boolean isAllInStock;


    @TableField(exist = false)
    private String itemProductId;
    @TableField(exist = false)
    private List<String> itemProductIds;
    @TableField(exist = false)
    private String itemSupplierId;
    @TableField(exist = false)
    private List<String> itemSupplierIds;
}
