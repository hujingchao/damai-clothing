package com.fenglei.model.basedata;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.BaseEntity;
import com.fenglei.model.system.entity.SysFiles;
import com.fenglei.model.workFlow.entity.ChildNode;
import com.fenglei.model.workFlow.entity.ChildNodeApprovalResult;
import com.fenglei.model.workFlow.entity.WorkFlow;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author ljw
 */
@Data
@ApiModel(value = "BdTempMaterial-物料临时", description = "物料临时类")
@TableName(value = "bd_temp_material")
public class BdTempMaterial extends BaseEntity {

    @ApiModelProperty(value = "货品编码")
    private String number;
    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "物料分类：0-成品；1-辅料；2-原材料；")
    private Integer materialGroup;

    @ApiModelProperty(value = "单位id")
    private String unitId;
    @TableField(exist = false)
    @ApiModelProperty(value = "单位")
    private String unitName;

    @ApiModelProperty(value = "备注")
    @TableField(fill = FieldFill.UPDATE)
    private String remark;

    @ApiModelProperty(value = "主图id")
    @TableField(fill = FieldFill.UPDATE)
    private String mainPicId;
    @TableField(exist = false)
    @ApiModelProperty("主图")
    private SysFiles mainPic;

    @TableField(exist = false)
    @ApiModelProperty(value = "图片信息")
    List<BdTempMaterialAttach> attaches;
    @TableField(exist = false)
    List<String> attachUrls;

    @ApiModelProperty(value = "工价模板id")
    @TableField(fill = FieldFill.UPDATE)
    private String wageId;

    @ApiModelProperty(value = "状态：0-未提交；1-流转中；2-已完成；3-重新提交")
    private Integer status;

    @ApiModelProperty(value = "最终审核时间")
    @TableField(fill = FieldFill.UPDATE)
    private String auditTime;

    @TableField(exist = false)
    @ApiModelProperty(value = "最终审核人姓名")
    private String auditor;

    @ApiModelProperty(value = "最终审核人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String auditorId;

    @TableField(exist = false)
    @ApiModelProperty("默认供应商")
    private BdTempMaterialSupplier defMaterialSupplier;

    @TableField(exist = false)
    @ApiModelProperty(value = "颜色/色号")
    List<BdTempMaterialColor> colors;

    @TableField(exist = false)
    @ApiModelProperty(value = "规格")
    List<BdTempMaterialSpecification> specifications;

    @TableField(exist = false)
    @ApiModelProperty(value = "供应商")
    List<BdTempMaterialSupplier> suppliers;

    @TableField(exist = false)
    @ApiModelProperty(value = "辅料信息")
    List<BdTempMaterialAux> auxes;

    @TableField(exist = false)
    @ApiModelProperty(value = "原材料信息")
    List<BdTempMaterialRaw> raws;

    @TableField(exist = false)
    @ApiModelProperty(value = "工序信息")
    List<BdTempMaterialRoute> routes;

    @TableField(exist = false)
    @ApiModelProperty(value = "工艺信息")
    List<BdTempMaterialProcess> processes;

    @TableField(exist = false)
    @ApiModelProperty(value = "辅料id")
    private String auxMaterialId;

    @TableField(exist = false)
    @ApiModelProperty(value = "原材料id")
    private String rawMaterialId;

    @TableField(exist = false)
    @ApiModelProperty(value = "工序id")
    private String procedureId;

    @TableField(exist = false)
    @ApiModelProperty(value = "员工id")
    private String staffId;

    @TableField(exist = false)
    @ApiModelProperty(value = "供应商id")
    private String supplierId;

    @TableField(exist = false)
    @ApiModelProperty(value = "工艺名称")
    private String process;

    @TableField(exist = false)
    private List<Integer> inMaterialGroup;

    @TableField(exist = false)
    private String mtrlDetailId;

    @TableField(exist = false)
    @ApiModelProperty(value = "商家编码")
    private String sjNumber;
    @TableField(exist = false)
    @ApiModelProperty(value = "颜色")
    private String color;
    @TableField(exist = false)
    @ApiModelProperty(value = "规格")
    private String specification;

    @TableField(exist = false)
    @ApiModelProperty(value = "属性详细")
    private List<BdTempMaterialDetail> details;

    @ApiModelProperty("工艺要求")
    private String content;

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

    @ApiModelProperty("原始id")
    private String originalId;

    @TableField(exist = false)
    @ApiModelProperty("原始id")
    private String origiDetailId;
}
