package com.fenglei.service.basedata.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.common.constant.Constants;
import com.fenglei.common.exception.BizException;
import com.fenglei.common.result.Result;
import com.fenglei.common.result.ResultCode;
import com.fenglei.common.util.RequestUtils;
import com.fenglei.mapper.basedata.BdTempWagesItemMapper;
import com.fenglei.mapper.basedata.BdTempWagesMapper;
import com.fenglei.mapper.basedata.BdWagesMapper;
import com.fenglei.model.basedata.*;
import com.fenglei.model.workFlow.dto.FlowDepartment;
import com.fenglei.model.workFlow.dto.FlowOperationInfo;
import com.fenglei.model.workFlow.dto.FlowRole;
import com.fenglei.model.workFlow.dto.FlowUser;
import com.fenglei.model.workFlow.entity.ChildNode;
import com.fenglei.service.basedata.*;
import com.fenglei.service.workFlow.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ljw
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BdTempWagesServiceImpl extends ServiceImpl<BdTempWagesMapper, BdTempWages> implements BdTempWagesService {

    @Resource
    private BdTempWagesItemService bdTempWagesItemService;
    @Resource
    private BdProcedureService bdProcedureService;
    @Resource
    private SystemInformationAcquisitionService systemInformationAcquisitionService;
    @Resource
    private CirculationOperationService circulationOperationService;

    @Resource
    private BdWagesService bdWagesService;
    @Resource
    private BdWagesItemService bdWagesItemService;

    @Resource
    private BdWagesMapper bdWagesMapper;
    @Resource
    private BdTempWagesItemMapper bdTempWagesItemMapper;

    @Override
    public IPage<BdTempWages> myPage(Page page, BdTempWages bdTempWages) {

        List<String> pids = new ArrayList<>();
        if (StringUtils.isNotEmpty(bdTempWages.getProcedureId())) {
            List<BdTempWagesItem> items = bdTempWagesItemService.list(
                    new LambdaQueryWrapper<BdTempWagesItem>()
                            .eq(BdTempWagesItem::getProcedureId, bdTempWages.getProcedureId())
            );
            if (items != null && items.size() > 0) {
                for (BdTempWagesItem item : items) {
                    if (!pids.contains(item.getPid())) {
                        pids.add(item.getPid());
                    }
                }
            } else {
                if (pids.size() == 0) {
                    pids.add("-1");
                }
            }
        }
        bdTempWages.setIds(pids);

        IPage<BdTempWages> iPage = baseMapper.getPage(page, bdTempWages);
        if (iPage != null) {
            List<BdTempWages> records = iPage.getRecords();
            if (records != null && records.size() > 0) {
                List<String> ids = records.stream().map(BdTempWages::getId).collect(Collectors.toList());
                BdTempWagesItem bdTempWagesItem = new BdTempWagesItem();
                bdTempWagesItem.setPids(ids);
                List<BdTempWagesItem> items = bdTempWagesItemMapper.getList(bdTempWagesItem);
                if (items != null && items.size() > 0) {
                    for (BdTempWages record : records) {
                        List<BdTempWagesItem> myItems = items.stream().filter(r -> r.getPid().equals(record.getId())).collect(Collectors.toList());
                        record.setBdWagesItems(myItems);
                    }
                }
            }
        }

        return iPage;
    }

    @Override
    public List<BdTempWages> myList(BdTempWages bdTempWages) {
        List<String> pids = new ArrayList<>();

        List<String> procedureIds = new ArrayList<>();
        if (StringUtils.isNotEmpty(bdTempWages.getCommFilter())) {
            List<BdProcedure> procedures = bdProcedureService.list(
                    new LambdaQueryWrapper<BdProcedure>()
                            .like(BdProcedure::getNumber, bdTempWages.getCommFilter())
                            .or()
                            .like(BdProcedure::getName, bdTempWages.getCommFilter())
            );
            if (procedures != null && procedures.size() > 0) {
                for (BdProcedure procedure : procedures) {
                    procedureIds.add(procedure.getId());
                }
            } else {
                procedureIds.add("-1");
            }
        }

        if (StringUtils.isNotEmpty(bdTempWages.getProcedureId())) {
            List<BdTempWagesItem> items = bdTempWagesItemService.list(
                    new LambdaQueryWrapper<BdTempWagesItem>()
                            .in(procedureIds != null && procedureIds.size() > 0, BdTempWagesItem::getProcedureId, procedureIds)
                            .eq(StringUtils.isNotEmpty(bdTempWages.getProcedureId()), BdTempWagesItem::getProcedureId, bdTempWages.getProcedureId())
            );
            if (items != null && items.size() > 0) {
                for (BdTempWagesItem item : items) {
                    if (!pids.contains(item.getPid())) {
                        pids.add(item.getPid());
                    }
                }
            } else {
                pids.add("-1");
            }
        }

        List<BdTempWages> list = this.list(
                new LambdaQueryWrapper<BdTempWages>()
                        .apply(StringUtils.isNotEmpty(bdTempWages.getCommFilter()), " (number like '%" + bdTempWages.getCommFilter() + "%' or name like '%" + bdTempWages.getCommFilter() + "%')")
                        .like(StringUtils.isNotEmpty(bdTempWages.getNumber()), BdTempWages::getNumber, bdTempWages.getNumber())
                        .like(StringUtils.isNotEmpty(bdTempWages.getName()), BdTempWages::getName, bdTempWages.getName())
                        .in(pids.size() > 0, BdTempWages::getId, pids)
                        .eq(ObjectUtils.isNotEmpty(bdTempWages.getStatus()), BdTempWages::getStatus, bdTempWages.getStatus())
                        .orderByDesc(BdTempWages::getCreateTime)
        );

        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BdTempWages add(BdTempWages bdTempWages) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        if (StringUtils.isNotEmpty(bdTempWages.getOriginalId())) {
            if (StringUtils.isNotEmpty(bdTempWages.getNumber())) {
                List<BdWages> list = bdWagesService.list(
                        new LambdaQueryWrapper<BdWages>()
                                .eq(BdWages::getNumber, bdTempWages.getNumber())
                                .ne(BdWages::getId, bdTempWages.getOriginalId())
                );
                if (list != null && list.size() > 0) {
                    throw new BizException("已存在相同编码的工价档案");
                }
            } else {
                BdWages wages = bdWagesService.getById(bdTempWages.getOriginalId());
                if (wages != null) {
                    bdTempWages.setNumber(wages.getNumber());
                }
            }
        }
        if (StringUtils.isEmpty(bdTempWages.getNumber())) {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            String no = "GJ" + year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + "-";
            List<String> nos = bdWagesMapper.getNos(no);
            if (nos != null && nos.size() > 0) {
                String maxNo = nos.get(0);
                Integer pos = maxNo.lastIndexOf("-");
                String maxIdxStr = maxNo.substring(pos + 1, pos + 5);
                Integer maxNoInt = Integer.valueOf(maxIdxStr);
                String noIdxStr = String.format("%04d", maxNoInt + 1);
                no = no + noIdxStr;
            } else {
                no = no + "0001";
            }
            bdTempWages.setNumber(no);
        }

        if (bdTempWages.getBdWagesItems() == null || bdTempWages.getBdWagesItems().size() == 0) {
            throw new BizException("未检测到明细");
        }

        bdTempWages.setStatus(Constants.INT_STATUS_CREATE);
        bdTempWages.setCreateTime(sdf.format(new Date()));
        bdTempWages.setCreatorId(RequestUtils.getUserId());
        bdTempWages.setCreator(RequestUtils.getNickname());
        bdTempWages.setEnabled(Constants.BOOL_STATUS_ENABLED);
        bdTempWages.setEnableTime(sdf.format(new Date()));
        bdTempWages.setEnablerId(RequestUtils.getUserId());
        bdTempWages.setEnabler(RequestUtils.getNickname());
        if (!this.save(bdTempWages)) {
            throw new BizException("保存失败");
        }

        List<BdTempWagesItem> items = bdTempWages.getBdWagesItems();
        for (BdTempWagesItem item : items) {
            item.setPid(bdTempWages.getId());
            item.setCreateTime(sdf.format(new Date()));
            item.setCreatorId(RequestUtils.getUserId());
            item.setCreator(RequestUtils.getNickname());
        }
        if (!bdTempWagesItemService.saveBatch(items)) {
            throw new BizException("明细保存失败");
        }

        return bdTempWages;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BdTempWages myUpdate(BdTempWages bdTempWages) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        if (StringUtils.isNotEmpty(bdTempWages.getOriginalId())) {
            if (StringUtils.isNotEmpty(bdTempWages.getNumber())) {
                List<BdWages> list = bdWagesService.list(
                        new LambdaQueryWrapper<BdWages>()
                                .eq(BdWages::getNumber, bdTempWages.getNumber())
                                .ne(BdWages::getId, bdTempWages.getOriginalId())
                );
                if (list != null && list.size() > 0) {
                    throw new BizException("已存在相同编码的工价档案");
                }
            } else {
                BdWages wages = bdWagesService.getById(bdTempWages.getOriginalId());
                if (wages != null) {
                    bdTempWages.setNumber(wages.getNumber());
                }
            }
        }
        if (StringUtils.isEmpty(bdTempWages.getNumber())) {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            String no = "GJ" + year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + "-";
            List<String> nos = bdWagesMapper.getNos(no);
            if (nos != null && nos.size() > 0) {
                String maxNo = nos.get(0);
                Integer pos = maxNo.lastIndexOf("-");
                String maxIdxStr = maxNo.substring(pos + 1, pos + 5);
                Integer maxNoInt = Integer.valueOf(maxIdxStr);
                String noIdxStr = String.format("%04d", maxNoInt + 1);
                no = no + noIdxStr;
            } else {
                no = no + "0001";
            }
            bdTempWages.setNumber(no);
        }

        if (bdTempWages.getBdWagesItems() == null || bdTempWages.getBdWagesItems().size() == 0) {
            throw new BizException("未检测到明细");
        }

        bdTempWages.setUpdateTime(sdf.format(new Date()));
        bdTempWages.setUpdaterId(RequestUtils.getUserId());
        bdTempWages.setUpdater(RequestUtils.getNickname());
        if (!this.updateById(bdTempWages)) {
            throw new BizException("更新失败");
        }

        // 删除旧数据
        List<BdTempWagesItem> oldItems = bdTempWagesItemService.list(
                new LambdaQueryWrapper<BdTempWagesItem>()
                        .eq(BdTempWagesItem::getPid, bdTempWages.getId())
        );
        if (oldItems != null && oldItems.size() > 0) {
            List<String> oldItemIds = oldItems.stream().map(BdTempWagesItem::getId).collect(Collectors.toList());
            if (oldItemIds != null && oldItemIds.size() > 0) {
                if (!bdTempWagesItemService.removeByIds(oldItemIds)) {
                    throw new BizException("明细更新失败，异常码1");
                }
            }
        }

        // 保存新数据
        List<BdTempWagesItem> newItems = bdTempWages.getBdWagesItems();
        for (BdTempWagesItem item : newItems) {
            item.setPid(bdTempWages.getId());
            item.setUpdateTime(sdf.format(new Date()));
            item.setUpdaterId(RequestUtils.getUserId());
            item.setUpdater(RequestUtils.getNickname());

            if (ObjectUtils.isEmpty(item.getCreatorId())) {
                item.setCreateTime(sdf.format(new Date()));
                item.setCreatorId(RequestUtils.getUserId());
                item.setCreator(RequestUtils.getNickname());
            }
        }
        if (!bdTempWagesItemService.saveOrUpdateBatch(newItems)) {
            throw new BizException("明细更新失败，异常码2");
        }

        return bdTempWages;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteById(String id) {
        BdTempWages bdTempWages = this.getById(id);
        if (bdTempWages == null) {
            throw new BizException("该工价模板不存在！");
        }
        if (bdTempWages.getStatus() == Constants.INT_STATUS_APPROVING || bdTempWages.getStatus() == Constants.INT_STATUS_AUDITED) {
            throw new BizException("流转中 和 已审核 数据无法删除");
        }

        if (!this.removeById(id)) {
            throw new BizException("删除失败，异常码1");
        }

        List<BdTempWagesItem> items = bdTempWagesItemService.list(
                new LambdaQueryWrapper<BdTempWagesItem>()
                        .eq(BdTempWagesItem::getPid, id)
        );
        if (items != null && items.size() > 0) {
            List<String> itemIds = items.stream().map(BdTempWagesItem::getId).collect(Collectors.toList());
            if (!bdTempWagesItemService.removeByIds(itemIds)) {
                throw new BizException("删除失败，异常码2");
            }
        }

        if (StringUtils.isNotEmpty(bdTempWages.getOriginalId())) {
            BdWages wages = bdWagesService.getById(bdTempWages.getOriginalId());
            if (wages != null && wages.getStatus() != 2) {
                bdWagesService.removeById(bdTempWages.getOriginalId());

                bdWagesItemService.remove(
                        new LambdaQueryWrapper<BdWagesItem>()
                                .eq(BdWagesItem::getPid, bdTempWages.getOriginalId())
                );
            }
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(String[] ids) {
        List<String> idList = Arrays.asList(ids);
        List<BdTempWages> list = this.list(
                new LambdaQueryWrapper<BdTempWages>()
                        .in(BdTempWages::getId, idList)
        );
        if (list != null && list.size() > 0) {
            List<String> delIds = list.stream().filter(r -> r.getStatus() == Constants.INT_STATUS_CREATE || r.getStatus() == Constants.INT_STATUS_RESUBMIT).map(BdTempWages::getId).collect(Collectors.toList());
            if (delIds != null && delIds.size() > 0) {
                if (!this.removeByIds(delIds)) {
                    throw new BizException("删除失败，异常码1");
                }
            } else {
                throw new BizException("流转中 及 已审核 数据无法删除");
            }

            List<BdTempWagesItem> items = bdTempWagesItemService.list(
                    new LambdaQueryWrapper<BdTempWagesItem>()
                            .in(BdTempWagesItem::getPid, delIds)
            );
            if (items != null && items.size() > 0) {
                List<String> itemIds = items.stream().map(BdTempWagesItem::getId).collect(Collectors.toList());
                if (!bdTempWagesItemService.removeByIds(itemIds)) {
                    throw new BizException("删除失败，异常码2");
                }
            }
        }
    }

    @Override
    public BdTempWages detail(String id) {
        BdTempWages result = baseMapper.infoById(id);
        if (result != null) {
            BdTempWagesItem bdTempWagesItem = new BdTempWagesItem();
            bdTempWagesItem.setPid(id);
            List<BdTempWagesItem> items = bdTempWagesItemMapper.getList(bdTempWagesItem);
            result.setBdWagesItems(items);
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BdTempWages submit(String id) throws Exception {
        BdTempWages result = this.detail(id);
        if (result == null) {
            throw new BizException("未检索到数据");
        }
        if (result.getStatus() != Constants.INT_STATUS_CREATE && result.getStatus() != Constants.INT_STATUS_RESUBMIT) {
            throw new BizException("提交失败，仅 '创建' 和 '重新审核' 状态允许提交");
        }

        result = this.auditUptData(result);
        return result;
    }

    private BdTempWages auditUptData(BdTempWages bdTempWages) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        bdTempWages.setStatus(Constants.INT_STATUS_AUDITED);
        bdTempWages.setAuditTime(sdf.format(new Date()));
        bdTempWages.setAuditorId(RequestUtils.getUserId());
        bdTempWages.setAuditor(RequestUtils.getNickname());
//        if (!this.updateById(bdTempWages)) {
//            throw new BizException("操作失败");
//        }

        // 生成正式数据
        BdTempWages thisTemp = this.detail(bdTempWages.getId());
        if (thisTemp != null) {
            BdWages wages = new BdWages();
            BeanUtils.copyProperties(wages, thisTemp);

            // 明细
            List<BdTempWagesItem> tempWagesItems = thisTemp.getBdWagesItems();
            if (tempWagesItems != null && tempWagesItems.size() > 0) {
                List<BdWagesItem> wagesItems = new ArrayList<>();

                for (BdTempWagesItem tempWagesItem : tempWagesItems) {
                    BdWagesItem wagesItem = new BdWagesItem();
                    BeanUtils.copyProperties(wagesItem, tempWagesItem);

                    wagesItem.setId(null);
                    wagesItem.setPid(thisTemp.getOriginalId());

                    wagesItems.add(wagesItem);
                }
                wages.setBdWagesItems(wagesItems);
            }

            if (StringUtils.isNotEmpty(wages.getOriginalId())) {
                wages.setId(wages.getOriginalId());
                wages.setStatus(Constants.INT_STATUS_AUDITED);

                BdWages result = bdWagesService.myUpdate(wages);
                if (result == null) {
                    throw new BizException("审核失败，请重试1");
                }
            } else {
                wages.setId(null);

                BdWages result = bdWagesService.add(wages);
                if (result == null) {
                    throw new BizException("审核失败，请重试2");
                } else {
                    result.setStatus(Constants.INT_STATUS_AUDITED);
                    if (!bdWagesService.updateById(result)) {
                        throw new BizException("审核失败，请重试4");
                    }
                }
            }

            if (!this.removeById(bdTempWages.getId())) {
                throw new BizException("审核失败，请重试3");
            }

            // 替换为正式id
            bdTempWages.setId(wages.getId());
        } else {
            throw new BizException("数据不存在");
        }

        return bdTempWages;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String batchSubmitByIds(String[] ids) throws Exception {
        List<String> idList = Arrays.asList(ids);
        List<BdTempWages> list = this.list(
                new LambdaQueryWrapper<BdTempWages>()
                        .in(BdTempWages::getId, idList)
        );
        if (list != null && list.size() > 0) {
            // 过滤 创建/重新审核 且 启用 的数据
            List<BdTempWages> submitList = list.stream().filter(r -> (r.getStatus() == Constants.INT_STATUS_CREATE || r.getStatus() == Constants.INT_STATUS_RESUBMIT) && r.getEnabled()).collect(Collectors.toList());
            if (submitList != null && submitList.size() > 0) {
                for (BdTempWages bdTempWages : submitList) {
                    this.submit(bdTempWages.getId());
                }
            }

            List<BdTempWages> errList = list.stream().filter(r -> (r.getStatus() == Constants.INT_STATUS_CREATE || r.getStatus() == Constants.INT_STATUS_RESUBMIT) && r.getEnabled() == Constants.BOOL_STATUS_UNENABLED).collect(Collectors.toList());
            if (errList != null && errList.size() > 0) {
                return "存在禁用数据无法提交";
            }

            return "提交成功";
        } else {
            throw new BizException("未选择数据");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result doAction(BdTempWages bdTempWages) throws Exception {
        if (bdTempWages.getStatus() == Constants.INT_STATUS_APPROVING && ObjectUtils.isNotEmpty(bdTempWages.getWorkFlow())) {
            FlowOperationInfo flowOperationInfo = new FlowOperationInfo();
            flowOperationInfo.setWorkFlowId(bdTempWages.getWorkFlowId());
            flowOperationInfo.setFormData(bdTempWages);
            flowOperationInfo.setUserId(bdTempWages.getUserId());
            flowOperationInfo.setChildNodes(bdTempWages.getChildNodes());
            flowOperationInfo.setCurrentNodeId(bdTempWages.getCurrentNodeId());
            flowOperationInfo.setChildNodeApprovalResult(bdTempWages.getChildNodeApprovalResult());
            if (StringUtils.isNotEmpty(flowOperationInfo.getWorkFlowId())) {
                // 提交
                List<FlowUser> flowUsers = systemInformationAcquisitionService.getFlowUsers(null);
                Boolean start = circulationOperationService.startFlow(flowOperationInfo.getWorkFlowId(), flowOperationInfo.getFormData(), flowOperationInfo.getUserId(), flowOperationInfo.getChildNodes(), flowUsers);
                if (!start) {
                    throw new BizException("流程提交错误");
                }
                bdTempWages.setWorkFlowId("");
            } else {
                // 审批流程
                List<FlowUser> flowUsers = systemInformationAcquisitionService.getFlowUsers(null);
                List<FlowRole> flowRoles = systemInformationAcquisitionService.getFlowRoles(null);
                List<FlowDepartment> flowDepartments = systemInformationAcquisitionService.getFlowDepartments(null);
                Boolean next = circulationOperationService.doNextFlow(flowOperationInfo.getFormData(), flowUsers, flowRoles, flowDepartments, flowOperationInfo.getChildNodeApprovalResult(), flowOperationInfo.getUserId());
                if (!next) {
                    throw new BizException("流程提交失败");
                }
            }
            List<String> ids = new ArrayList<>();
            ids.add(bdTempWages.getId());
            List<ChildNode> currentNodes = getCurrentNodes(ids, bdTempWages.getWorkFlow().getId());
            bdTempWages.setWorkFlowInstantiateStatus(currentNodes.get(0).getWorkFlowInstantiateStatus());
            bdTempWages.setNodeStatus(currentNodes.get(0).getStatus());
            bdTempWages.setCurrentNodeId(currentNodes.get(0).getId());
            // 审批流正常结束
            if (circulationOperationService.whetherLast(bdTempWages.getId()) == 1) {
                bdTempWages = this.auditUptData(bdTempWages);

                return Result.success(bdTempWages);
            }
            // 驳回
            if (circulationOperationService.whetherLast(bdTempWages.getId()) == 2) {
                bdTempWages.setStatus(Constants.INT_STATUS_RESUBMIT);
            }
        }

        if (!this.updateById(bdTempWages)) {
            throw new BizException("操作失败");
        }

        return Result.success(bdTempWages);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result batchDoAction(BdTempWages bdTempWages) throws Exception {
        List<String> ids = bdTempWages.getIds();
        List<BdTempWages> wages = this.list(
                new LambdaQueryWrapper<BdTempWages>()
                        .in(BdTempWages::getId, ids)
        );
        if (wages != null && wages.size() > 0) {
            List<ChildNode> childNodes = getCurrentNodes(ids, bdTempWages.getWorkFlow().getId());
            for (int i = 0; i < wages.size(); i++) {
                BdTempWages item = wages.get(i);
                item.setStatus(bdTempWages.getStatus());
                item.setWorkFlowId(bdTempWages.getWorkFlowId());
                item.setUserId(bdTempWages.getUserId());
                item.setChildNodes(bdTempWages.getChildNodes());
                item.setChildNodeApprovalResult(bdTempWages.getChildNodeApprovalResult());
                item.setWorkFlow(bdTempWages.getWorkFlow());
                for (int j = 0; j < childNodes.size(); j++) {
                    if (childNodes.get(j).getFromId().equals(item.getId())) {
                        item.setWorkFlowInstantiateStatus(childNodes.get(j).getWorkFlowInstantiateStatus());
                        item.setNodeStatus(childNodes.get(j).getStatus());
                        item.setCurrentNodeId(childNodes.get(j).getId());
                        break;
                    }
                }
                Result result = doAction(item);
                if (!ResultCode.SUCCESS.getCode().equalsIgnoreCase(result.getCode())) {
                    throw new BizException("操作失败");
                }
            }
        }

        return Result.success("操作成功");
    }

    private List<ChildNode> getCurrentNodes(List<String> ids, String workFlowId){
        FlowOperationInfo flowOperationInfo =new FlowOperationInfo();
        FlowUser flowUser = new FlowUser();
        flowUser.setEmployeeName(RequestUtils.getNickname());
        flowUser.setId(RequestUtils.getUserId());
        flowOperationInfo.setFlowUser(flowUser);
        flowOperationInfo.setFormIds(ids);
        flowOperationInfo.setWorkFlowId(workFlowId);

        List<ChildNode> childNodeList = circulationOperationService.getCurrentNodes(flowOperationInfo);
        return childNodeList;
    }

    @Override
    public BdTempWages unAudit(String id) {
        BdTempWages result = this.detail(id);
        if (result == null) {
            throw new BizException("未检索到数据");
        }
        if (result.getStatus() != Constants.INT_STATUS_AUDITED) {
            throw new BizException("反审核失败，仅 '已完成' 状态允许反审核");
        }
        result.setStatus(Constants.INT_STATUS_RESUBMIT);
        result.setAuditTime(null);
        result.setAuditorId(null);
        result.setAuditor(null);
        if (!this.updateById(result)) {
            throw new BizException("反审核失败");
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String batchUnAuditByIds(String[] ids) {
        List<String> idList = Arrays.asList(ids);
        List<BdTempWages> list = this.list(
                new LambdaQueryWrapper<BdTempWages>()
                        .in(BdTempWages::getId, idList)
        );
        if (list != null && list.size() > 0) {
            List<BdTempWages> unAuditList = list.stream().filter(r -> r.getStatus() == Constants.INT_STATUS_AUDITED && r.getEnabled()).collect(Collectors.toList());
            if (unAuditList != null && unAuditList.size() > 0) {
                for (BdTempWages bdTempWages : unAuditList) {
                    this.unAudit(bdTempWages.getId());
                }
            }

            List<BdTempWages> errList = list.stream().filter(r -> r.getStatus() == Constants.INT_STATUS_AUDITED && r.getEnabled() == Constants.BOOL_STATUS_UNENABLED).collect(Collectors.toList());
            if (errList != null && errList.size() > 0) {
                return "存在禁用数据无法反审核";
            }

            return "反审核成功";
        } else {
            throw new BizException("未选择数据");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BdTempWages cancel(String id) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        BdTempWages result = this.detail(id);
        if (result == null) {
            throw new BizException("未检索到数据");
        }
        if (result.getStatus() == Constants.INT_STATUS_APPROVING) {
            throw new BizException("禁用失败，流转中数据无法禁用");
        }

        if (result.getEnabled() == Constants.BOOL_STATUS_UNENABLED) {
//            throw new BizException("禁用失败，仅启用状态允许禁用");
        } else {
            result.setEnabled(Constants.BOOL_STATUS_UNENABLED);
            result.setEnableTime(sdf.format(new Date()));
            result.setEnablerId(RequestUtils.getUserId());
            result.setEnabler(RequestUtils.getNickname());
            if (!this.updateById(result)) {
                throw new BizException("禁用失败");
            }
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String batchCancelByIds(String[] ids) {
        List<String> idList = Arrays.asList(ids);
        List<BdTempWages> list = this.list(
                new LambdaQueryWrapper<BdTempWages>()
                        .in(BdTempWages::getId, idList)
        );
        if (list != null && list.size() > 0) {
            List<BdTempWages> cancelList = list.stream().filter(r -> r.getEnabled() == Constants.BOOL_STATUS_ENABLED && r.getStatus() != Constants.INT_STATUS_APPROVING).collect(Collectors.toList());
            if (cancelList != null && cancelList.size() > 0) {
                for (BdTempWages bdTempWages : cancelList) {
                    this.cancel(bdTempWages.getId());
                }
            }

            if (cancelList.size() == list.size()) {
                return "禁用成功";
            } else {
                return "禁用成功，部分数据处于流转中，无法禁用";
            }
        } else {
            throw new BizException("未选择数据");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BdTempWages unCancel(String id) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        BdTempWages result = this.detail(id);
        if (result == null) {
            throw new BizException("未检索到数据");
        }
        if (result.getStatus() == Constants.INT_STATUS_APPROVING) {
            throw new BizException("反禁用失败，流转中数据无法禁用");
        }

        if (result.getEnabled()) {
//            throw new BizException("反禁用失败，仅禁用状态允许反禁用");
        } else {
            result.setEnabled(Constants.BOOL_STATUS_ENABLED);
            result.setEnableTime(sdf.format(new Date()));
            result.setEnablerId(RequestUtils.getUserId());
            result.setEnabler(RequestUtils.getNickname());
            if (!this.updateById(result)) {
                throw new BizException("反禁用失败");
            }
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String batchUnCancelByIds(String[] ids) {
        List<String> idList = Arrays.asList(ids);
        List<BdTempWages> list = this.list(
                new LambdaQueryWrapper<BdTempWages>()
                        .in(BdTempWages::getId, idList)
        );
        if (list != null && list.size() > 0) {
            List<BdTempWages> unCancelList = list.stream().filter(r -> r.getEnabled() == Constants.BOOL_STATUS_UNENABLED && r.getStatus() != Constants.INT_STATUS_APPROVING).collect(Collectors.toList());
            if (unCancelList != null && unCancelList.size() > 0) {
                for (BdTempWages bdTempWages : unCancelList) {
                    this.unCancel(bdTempWages.getId());
                }
            }

            if (unCancelList.size() == list.size()) {
                return "反禁用成功";
            } else {
                return "反禁用成功，部分数据处于流转中，无法反禁用";
            }
        } else {
            throw new BizException("未选择数据");
        }
    }
}
