package com.fenglei.model.prd.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.UBillBaseEntity;
import com.fenglei.model.system.entity.SysFiles;
import com.fenglei.model.system.entity.SysUser;
import com.fenglei.model.workFlow.entity.ChildNode;
import com.fenglei.model.workFlow.entity.ChildNodeApprovalResult;
import com.fenglei.model.workFlow.entity.WorkFlow;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author ljw
 */
@Data
@ApiModel(value = "PrdMo-生产订单", description = "生产订单类")
@TableName(value = "prd_mo")
public class PrdMo extends UBillBaseEntity {

    @ApiModelProperty("置顶：0-置顶；1-不置顶")
    private Integer top = 1;

    @ApiModelProperty(value = "关联生产订单，id")
    private String prdMoLinkId;
    @TableField(exist = false)
    @ApiModelProperty(value = "关联生产订单，编码")
    private String prdMoLinkNo;

    @ApiModelProperty(value = "关联采购订单，id")
    private String purPurchaseOrderLinkId;
    @TableField(exist = false)
    @ApiModelProperty(value = "关联采购订单，编码")
    private String purPurchaseOrderLinkNo;

    @ApiModelProperty("单据类型")
    private String billType;
    @TableField(exist = false)
    @ApiModelProperty("单据类型，用于过滤")
    private List<String> billTypes;

    @TableField(exist = false)
    @ApiModelProperty("生产订单数量")
    private Integer moCount;
    @TableField(exist = false)
    @ApiModelProperty("计划单数量")
    private Integer planCount;

    @ApiModelProperty("大货款号，id")
    private String productId;
    @TableField(exist = false)
    @ApiModelProperty("大货款号，编码")
    private String productNum;
    @TableField(exist = false)
    @ApiModelProperty("大货款号，名称")
    private String productName;
    @TableField(exist = false)
    @ApiModelProperty("大货款号s，ids")
    private List<String> productIds;

    @ApiModelProperty("单位id")
    private String unitId;
    @TableField(exist = false)
    @ApiModelProperty(value = "单位")
    private String unitName;

    @ApiModelProperty("负责人id")
    private String leaderId;
    @TableField(exist = false)
    @ApiModelProperty(value = "负责人")
    private String leader;
    @TableField(exist = false)
    @ApiModelProperty("负责人ids")
    private List<String> leaderIds;

    @ApiModelProperty("跟单员id")
    private String followerId;
    @TableField(exist = false)
    @ApiModelProperty(value = "跟单员")
    private String follower;
    @TableField(exist = false)
    @ApiModelProperty("跟单员id")
    private List<String> followerIds;

    @ApiModelProperty("预开工日")
    private String preBeginDate;
    @TableField(exist = false)
    @ApiModelProperty("预开工日(开始)，用于过滤")
    private String preBeginDateBegin;
    @TableField(exist = false)
    @ApiModelProperty("预开工日(结束)，用于过滤")
    private String preBeginDateEnd;

    @ApiModelProperty("预完工日")
    private String preEndDate;
    @TableField(exist = false)
    @ApiModelProperty("预完工日(开始)，用于过滤")
    private String preEndDateBegin;
    @TableField(exist = false)
    @ApiModelProperty("预完工日(结束)，用于过滤")
    private String preEndDateEnd;

    @ApiModelProperty("交货日期")
    private String deliveryDate;
    @TableField(exist = false)
    @ApiModelProperty("预完工日(开始)，用于过滤")
    private String deliveryDateBegin;
    @TableField(exist = false)
    @ApiModelProperty("预完工日(结束)，用于过滤")
    private String deliveryDateEnd;

    @ApiModelProperty("标签")
    private String tags;
    @TableField(exist = false)
    private List<String> tagList;

    @ApiModelProperty(value = "主图id")
    @TableField(fill = FieldFill.UPDATE)
    private String mainPicId;
    @TableField(exist = false)
    @ApiModelProperty("主图")
    private SysFiles mainPic;

    @ApiModelProperty(value = "状态：0-待生产；1-生产中；2-已生产；")
    private Integer productionStatus;
    @TableField(exist = false)
    private List<Integer> inProductionStatus;

    @ApiModelProperty("工艺要求")
    private String techRequire;

    @ApiModelProperty("备注信息")
    private String remark;

    @TableField(exist = false)
    @ApiModelProperty("规格详情标题信息")
    private List<String> moSpecs;

    @TableField(exist = false)
    @ApiModelProperty(value = "颜色规格信息")
    private List<PrdMoColorSpecData> moColorSpecDatas;

    @TableField(exist = false)
    @ApiModelProperty("规格颜色数据（前台传递形式）")
    private List<Map<String, Object>> moColorSpecMaps;

    @TableField(exist = false)
    private BigDecimal sumQty;

    @TableField(exist = false)
    @ApiModelProperty(value = "生产工序信息")
    private List<PrdMoProcess> moProcesses;

    @TableField(exist = false)
    private Integer procedureCount;
    @TableField(exist = false)
    private BigDecimal procedureSumAmount;

    @TableField(exist = false)
    @ApiModelProperty("其他成本标题信息")
    private List<String> moOtherCostLabels;

    @TableField(exist = false)
    @ApiModelProperty(value = "其他成本信息")
    private List<PrdMoOtherCost> moOtherCosts;

    @TableField(exist = false)
    @ApiModelProperty("其他成本数据（前台传递形式）")
    private List<Map<String, Object>> moOtherCostMaps;

    @TableField(exist = false)
    @ApiModelProperty("成品尺寸标题信息")
    private List<String> moFinSizeLabels;

    @TableField(exist = false)
    @ApiModelProperty(value = "成品尺寸信息")
    private List<PrdMoFinSizeData> moFinSizeDatas;

    @TableField(exist = false)
    @ApiModelProperty("成品尺寸数据（前台传递形式）")
    private List<Map<String, Object>> moFinSizeMaps;

    @TableField(exist = false)
    @ApiModelProperty(value = "辅料详情")
    private List<PrdMoMaterialAux> moMaterialAuxs;

    @TableField(exist = false)
    @ApiModelProperty(value = "原料详情")
    private List<PrdMoMaterialDetail> moMaterialDetails;

    @TableField(exist = false)
    @ApiModelProperty(value = "二次工艺")
    private List<PrdMoSecondaryProcess> moSecondaryProcesses;

    @TableField(exist = false)
    @ApiModelProperty(value = "图片信息")
    List<PrdMoAttachImg> attachImgs;
    @TableField(exist = false)
    List<String> attachImgUrls;

    @TableField(exist = false)
    @ApiModelProperty(value = "文件信息")
    List<PrdMoAttachFile> attachFiles;
    @TableField(exist = false)
    List<String> attachFileUrls;


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
    @ApiModelProperty("款数")
    private Integer finMtrlCount;
    @TableField(exist = false)
    @ApiModelProperty("单数")
    private Integer count;
    @TableField(exist = false)
    @ApiModelProperty("件数")
    private BigDecimal allQty;
    @TableField(exist = false)
    @ApiModelProperty("裁床数")
    private BigDecimal allCuttingQty;

    @TableField(exist = false)
    private BigDecimal allInstockQty;
    @TableField(exist = false)
    private BigDecimal allInstockProcessAmt;

    @TableField(exist = false)
    private BigDecimal allIngQty;
    @TableField(exist = false)
    private BigDecimal allIngProcessAmt;

    @TableField(exist = false)
    private BigDecimal cuttingQty;

    @TableField(exist = false)
    private BigDecimal inStockQty;

    @ApiModelProperty("关闭状态")
    private Boolean closeStatus;
}
